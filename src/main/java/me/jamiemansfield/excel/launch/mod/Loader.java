/*
 * This file is part of ExcelLoader, licensed under the MIT License (MIT).
 *
 * Copyright (c) Jamie Mansfield <https://www.jamierocks.uk/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.jamiemansfield.excel.launch.mod;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.jamiemansfield.excel.ExcelLoader;
import me.jamiemansfield.excel.launch.mod.system.IModSystem;
import me.jamiemansfield.excel.launch.mod.system.ModSystemCandidate;
import me.jamiemansfield.excel.mod.ModContainer;
import me.jamiemansfield.excel.mod.descriptor.DescriptorFormat;
import me.jamiemansfield.excel.mod.descriptor.ModJsonFormat;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * The actual mod loader of all this, that will handle reading mods
 * and passing them along to the appropriate mod system for creating
 * an instance of the mod.
 */
public final class Loader {

    private static boolean initialised = false;
    private static Path MODS_DIR;
    private static Path MOD_SYSTEMS_DIR;

    /**
     * A {@link Map} for translating between mod system ids and the mod system
     * itself.
     */
    private static final Map<String, IModSystem> modSystems = Maps.newHashMap();

    /**
     * A {@link Map} for translating between descriptor file names, and the
     * descriptor format itself.
     */
    private static final Map<String, DescriptorFormat> descriptorFormats = Maps.newHashMap();

    static {
        // Register the mod.json descriptor format
        registerDescriptorFormat(new ModJsonFormat());
    }

    /**
     * Initialises the ExcelLoader mod loader, in preparation of actually
     * loading things.
     */
    public static void init(final Path gamePath) {
        if (initialised) throw new RuntimeException("Loader has already been initialised!");

        // Get the directories we'll need in a bit
        MODS_DIR = gamePath.resolve("mods");
        MOD_SYSTEMS_DIR = gamePath.resolve("mod-systems");

        // Lets ensure those directories exist
        if (!Files.exists(MODS_DIR)) {
            try {
                Files.createDirectory(MODS_DIR);
            } catch (final IOException ex) {
                ExcelLoader.log.warn("Failed to create the 'mods' directory!", ex);
            }
        }
        if (!Files.exists(MOD_SYSTEMS_DIR)) {
            try {
                Files.createDirectory(MOD_SYSTEMS_DIR);
            } catch (final IOException ex) {
                ExcelLoader.log.warn("Failed to create the 'mod-systems' directory!", ex);
            }
        }

        // boom, we're done
        initialised = true;
    }

    /**
     * Registers the given descriptor format
     *
     * @param descriptorFormat The descriptor format
     */
    public static void registerDescriptorFormat(final DescriptorFormat descriptorFormat) {
        descriptorFormats.put(descriptorFormat.getFileName(), descriptorFormat);
    }

    /**
     * Finds mod system candidates, and loads them.
     *
     * @param loader The {@link LaunchClassLoader} to pass through to
     *               {@link IModSystem#injectIntoClassLoader(LaunchClassLoader)}
     */
    public static void loadModSystems(final LaunchClassLoader loader) {
        if (!initialised) throw new RuntimeException("Loader has not been initialised!");

        final List<ModSystemCandidate> systemCandidates = Lists.newArrayList();
        final List<Path> modSystemPaths = Lists.newArrayList();

        try {
            Files.walk(MOD_SYSTEMS_DIR)
                    .filter(file -> file.endsWith(".jar"))
                    .forEach(modSystemPaths::add);
        } catch (final IOException ex) {
            ExcelLoader.log.warn("Failed to walk the 'mod-systems' directory!", ex);
        }

        for (final Path modSystemPath : modSystemPaths) {
            try (final JarFile modSystemJar = new JarFile(modSystemPath.toFile())) {
                final ZipEntry modSystemPropertiesEntry = modSystemJar.getEntry("mod-system.properties");
                if (modSystemPropertiesEntry == null) continue;

                final Properties modSystemProperties = new Properties();
                modSystemProperties.load(modSystemJar.getInputStream(modSystemPropertiesEntry));

                final String id = modSystemProperties.getProperty("id");
                final String mainClass = modSystemProperties.getProperty("main-class");
                if (Strings.isNullOrEmpty(id) || Strings.isNullOrEmpty(mainClass)) continue;

                systemCandidates.add(new ModSystemCandidate(modSystemPath, id, mainClass));
            } catch (final IOException ex) {
                ExcelLoader.log.warn("Failed to open a potential mod system!", ex);
            }
        }

        ExcelLoader.log.info("Found {} mod system candidates", systemCandidates.size());

        for (final ModSystemCandidate modSystemCandidate : systemCandidates) {
            try {
                loader.addURL(modSystemCandidate.getSource().toUri().toURL());
            } catch (final MalformedURLException ex) {
                ExcelLoader.log.warn("Failed to load a mod system candidate!", ex);
                continue;
            }

            try {
                final Class<?> modSystemClass = loader.findClass(modSystemCandidate.getMainClass());
                if (!modSystemClass.isAssignableFrom(IModSystem.class)) {
                    ExcelLoader.log.warn("{} does not implement IModSystem!", modSystemCandidate.getMainClass());
                    continue;
                }
                final IModSystem modSystem = (IModSystem) modSystemClass.getConstructor().newInstance();
                modSystem.injectIntoClassLoader(loader);
                modSystem.provideDescriptorFormats().forEach(Loader::registerDescriptorFormat);

                modSystems.put(modSystemCandidate.getId(), modSystem);
            } catch (final ClassNotFoundException | IllegalAccessException | InstantiationException |
                    NoSuchMethodException | InvocationTargetException ex) {
                ExcelLoader.log.warn("Failed to load a mod system candidate!", ex);
            }
        }

        ExcelLoader.log.info("Loaded {} mod systems", modSystems.size());
    }

    public static void loadMods(final LaunchClassLoader loader) {
        if (!initialised) throw new RuntimeException("Loader has not been initialised!");

        final List<ModCandidate> modCandidates = Lists.newArrayList();
        final List<Path> modPaths = Lists.newArrayList();

        try {
            Files.walk(MODS_DIR)
                    .filter(file -> file.endsWith(".jar"))
                    .forEach(modPaths::add);
        } catch (final IOException ex) {
            ExcelLoader.log.warn("Failed to walk the 'mods' directory!", ex);
        }

        for (final Path modPath : modPaths) {
            try (final JarFile modJar = new JarFile(modPath.toFile())) {
                // Find all of the descriptors in the jar
                final Set<ZipEntry> descriptorEntries = descriptorFormats.keySet().stream()
                        .map(modJar::getEntry)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                for (final ZipEntry descriptorEntry : descriptorEntries) {
                    final DescriptorFormat format = descriptorFormats.get(descriptorEntry.getName());
                    // Find all of the mod candidates in the jar
                    format.parse(modJar.getInputStream(descriptorEntry))
                            .forEach(descriptor -> modCandidates.add(new ModCandidate(modPath, descriptor)));
                }
            } catch (final IOException ex) {
                ExcelLoader.log.warn("Failed to open a potential mod!", ex);
            }
        }

        ExcelLoader.log.info("Found {} mod candidates", modCandidates.size());

        for (final ModCandidate candidate : modCandidates) {
            final String modSystemId = candidate.getDescriptor().getSystem();

            final IModSystem modSystem = modSystems.get(modSystemId);
            if (modSystem == null) {
                ExcelLoader.log.warn("The mod '{}' uses an uninstalled mod system '{}'!", candidate.getDescriptor().getName(), modSystemId);
                continue;
            }

            final Optional<ModContainer> container = modSystem.load(candidate);
            if (!container.isPresent()) continue;

            ExcelLoader.modManager.registerMod(container.get());
        }

        ExcelLoader.log.info("Loaded {} mods", ExcelLoader.modManager.getMods().size());
    }

    private Loader() {
    }

}

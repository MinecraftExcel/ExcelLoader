/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.jamiemansfield.excel.ExcelLoader;
import me.jamiemansfield.excel.launch.mod.system.ExcelModSystem;
import me.jamiemansfield.excel.launch.mod.system.IModSystem;
import me.jamiemansfield.excel.launch.mod.system.ModSystemCandidate;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;
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

    static {
        // Register our mod system
        modSystems.put("Excel", new ExcelModSystem());
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

                modSystems.put(modSystemCandidate.getId(), modSystem);
            } catch (final ClassNotFoundException | IllegalAccessException | InstantiationException |
                    NoSuchMethodException | InvocationTargetException ex) {
                ExcelLoader.log.warn("Failed to load a mod system candidate!", ex);
            }
        }

        ExcelLoader.log.info("Loaded {} mod systems", modSystems.size());
    }

    private Loader() {
    }

}

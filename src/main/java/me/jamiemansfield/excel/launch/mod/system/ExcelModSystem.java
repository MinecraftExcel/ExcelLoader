/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod.system;

import com.google.inject.Guice;
import com.google.inject.Injector;
import joptsimple.internal.Strings;
import me.jamiemansfield.excel.ExcelLoader;
import me.jamiemansfield.excel.launch.mod.ModCandidate;
import me.jamiemansfield.excel.mod.ModContainer;
import net.minecraft.launchwrapper.Launch;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * The <pre>Excel</pre> mod system implementation.
 */
public class ExcelModSystem implements IModSystem {

    @Override
    public Optional<ModContainer> load(final ModCandidate modCandidate) {
        try (final ZipFile zipFile = new ZipFile(modCandidate.getSource().toFile())) {
            final ZipEntry modProperties =
                    zipFile.getEntry("META-INF/excel-mods/" + modCandidate.getDescriptor().getId() + ".properties");
            if (modProperties == null) return Optional.empty();

            final Properties properties = new Properties();
            properties.load(zipFile.getInputStream(modProperties));

            final String mainClass = properties.getProperty("main-class");
            if (Strings.isNullOrEmpty(mainClass)) return Optional.empty();

            try {
                Launch.classLoader.addURL(modCandidate.getSource().toFile().toURI().toURL());
                final Injector injector = Guice.createInjector(new ExcelModGuiceModule(modCandidate.getDescriptor()));
                final Class<?> clazz = Launch.classLoader.findClass(mainClass);
                final Object instance = injector.getInstance(clazz);
                return Optional.of(new ExcelModContainer(modCandidate.getDescriptor(), instance));
            } catch (final ClassNotFoundException ex) {
                ExcelLoader.log.warn("Failed to find the mod class!", ex);
                return Optional.empty();
            }
        } catch (final IOException ex) {
            ExcelLoader.log.warn("Failed to load a mod candidate!", ex);
            return Optional.empty();
        }
    }

}

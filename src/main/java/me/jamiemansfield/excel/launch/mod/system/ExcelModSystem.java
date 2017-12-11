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

package me.jamiemansfield.excel.launch.mod.system;

import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
                Launch.classLoader.addURL(modCandidate.getSource().toUri().toURL());
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

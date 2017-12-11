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

package me.jamiemansfield.excel.mod.processor;

import static javax.tools.StandardLocation.CLASS_OUTPUT;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.jamiemansfield.excel.SharedConstants;
import me.jamiemansfield.excel.mod.Mod;
import me.jamiemansfield.excel.mod.ModDescriptor;
import me.jamiemansfield.excel.util.ap.AnnotationProcessor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * An annotation processor designed to produce <code>mod.json</code>
 * files for ExcelLoader mods.
 *
 * Should a mod developer choose to produce the mod descriptor themselves,
 * the annotation processor will acknowledge that, and not recreate the file.
 */
@SupportedAnnotationTypes("me.jamiemansfield.excel.mod.Mod")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ModProcessor extends AnnotationProcessor {

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(ModDescriptor.class, new ModDescriptor.Serialiser())
            .create();

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        // Lets not waste time if there aren't any mods to process
        if (this.contains(annotations, Mod.class)) return false;

        // Check if a mod descriptor already exists
        // TODO: implement

        // Process the mods
        final List<ModDescriptor> descriptors = Lists.newArrayList();
        final Map<String, Properties> properties = Maps.newHashMap();
        for (final Element annotatedElement : roundEnv.getElementsAnnotatedWith(Mod.class)) {
            // First, check that the element is a class
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                this.error("Only classes can be annotated with @Mod", annotatedElement);
                continue;
            }

            // Lets grab that annotation
            final TypeElement pluginElement = (TypeElement) annotatedElement;
            final Mod mod = pluginElement.getAnnotation(Mod.class);

            // Check the mod id
            if (mod.id().isEmpty()) {
                this.error("The mod identifier cannot be empty!", annotatedElement);
                continue;
            }

            // We're good to go
            final ModDescriptor descriptor = new ModDescriptor(
                    "Excel",
                    mod.id(),
                    mod.name(),
                    mod.version()
            );
            final Properties props = new Properties();
            props.put("main-class", pluginElement.getQualifiedName().toString());

            descriptors.add(descriptor);
            properties.put(mod.id(), props);
        }

        // Don't process an empty descriptor file
        if (descriptors.isEmpty()) return false;

        // Lets do some writing
        try (final BufferedWriter writer =
                new BufferedWriter(this.processingEnv.getFiler().createResource(CLASS_OUTPUT, "", "mod.json").openWriter())) {
            writer.write(gson.toJson(descriptors));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        for (final Map.Entry<String, Properties> entry : properties.entrySet()) {
            try (final BufferedWriter writer =
                    new BufferedWriter(this.processingEnv.getFiler().createResource(CLASS_OUTPUT, "META-INF/excel-mods", entry.getKey()).openWriter())) {
                entry.getValue().store(writer, "Generated by ExcelLoader v" + SharedConstants.VERSION);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

}

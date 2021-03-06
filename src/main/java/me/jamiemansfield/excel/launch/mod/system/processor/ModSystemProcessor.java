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

package me.jamiemansfield.excel.launch.mod.system.processor;

import static javax.tools.StandardLocation.CLASS_OUTPUT;

import me.jamiemansfield.excel.SharedConstants;
import me.jamiemansfield.excel.launch.mod.system.ModSystem;
import me.jamiemansfield.excel.util.ap.AnnotationProcessor;

import java.io.BufferedWriter;
import java.io.IOException;
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
 * An annotation processor designed to produce <code>mod-system.properties</code>
 * files for ExcelLoader mod systems.
 *
 * Should a mod developer choose to produce the mod system descriptor themselves,
 * the annotation processor will acknowledge that, and not recreate the file.
 */
@SupportedAnnotationTypes("me.jamiemansfield.excel.launch.mod.system.ModSystem")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ModSystemProcessor extends AnnotationProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        // Lets not waste time if there aren't any mod systems to process
        if (this.contains(annotations, ModSystem.class)) return false;

        // Check if a mod system descriptor already exists
        // TODO: implement

        // Process the mod systems
        for (final Element annotatedElement : roundEnv.getElementsAnnotatedWith(ModSystem.class)) {
            // First, check that the element is a class
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                this.error("Only classes can be annotated with @ModSystem", annotatedElement);
                return false; // There should only be one mod system per jar!
            }

            // Lets grab that annotation
            final TypeElement pluginElement = (TypeElement) annotatedElement;
            final ModSystem modSystem = pluginElement.getAnnotation(ModSystem.class);

            // Check the mod system id
            if (modSystem.value().isEmpty()) {
                this.error("The mod identifier cannot be empty!", annotatedElement);
                return false; // There should only be one mod system per jar!
            }

            // We're good to go
            final Properties properties = new Properties();
            properties.setProperty("id", modSystem.value());
            properties.setProperty("main-class", pluginElement.getQualifiedName().toString());
            try (final BufferedWriter writer =
                    new BufferedWriter(this.processingEnv.getFiler().createResource(CLASS_OUTPUT, "", "mod-system.properties").openWriter())) {
                properties.store(writer, "Generated by ExcelLoader v" + SharedConstants.VERSION);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
            return false; // There should only be one mod system per jar!
        }

        return false;
    }

}

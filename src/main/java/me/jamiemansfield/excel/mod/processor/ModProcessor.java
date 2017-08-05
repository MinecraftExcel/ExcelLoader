/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.mod.processor;

import static javax.tools.StandardLocation.CLASS_OUTPUT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import me.jamiemansfield.excel.mod.Mod;
import me.jamiemansfield.excel.mod.ModDescriptor;
import me.jamiemansfield.excel.util.ap.AnnotationProcessor;

import java.io.BufferedWriter;
import java.io.IOException;
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
        final JsonArray descriptors = new JsonArray();
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

            // Were good to go
            descriptors.add(gson.toJsonTree(new ModDescriptor(
                    pluginElement.getQualifiedName().toString(),
                    mod.id(),
                    mod.name(),
                    mod.version()
            )));
        }

        // Don't process an empty descriptor file
        if (descriptors.size() == 0) return false;

        // Lets do some writing
        try (BufferedWriter writer
                = new BufferedWriter(this.processingEnv.getFiler().createResource(CLASS_OUTPUT, "", "mod.json").openWriter())) {
            writer.write(descriptors.toString());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
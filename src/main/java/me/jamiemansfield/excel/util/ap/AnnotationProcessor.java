/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.util.ap;

import java.util.Collection;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * An extension to {@link AbstractProcessor}, to provide a number of utility functions
 * pertaining to annotation processors such as logging.
 */
public abstract class AnnotationProcessor extends AbstractProcessor {

    protected AnnotationProcessor() {
    }

    /**
     * Convenience method for printing an error message.
     *
     * @param message The message
     * @param element The element
     */
    protected void error(final String message, final Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * Convenience method for printing a warning message.
     *
     * @param message The message
     * @param element The element
     */
    protected void warning(final String message, final Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * Convenience method for printing a mandatory warning message.
     *
     * @param message The message
     * @param element The element
     */
    protected void mandatoryWarning(final String message, final Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * Convenience method for printing a note message.
     *
     * @param message The message
     * @param element The element
     */
    protected void note(final String message, final Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * Convenience method for printing a other message.
     *
     * @param message The message
     * @param element The element
     */
    protected void other(final String message, final Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * Establishes whether the given collection of elements, contains the class.
     *
     * @param elements The elements
     * @param clazz The class
     * @return {@code True} if the collection contains the given class,
     *         {@code false} otherwise
     */
    protected boolean contains(final Collection<? extends TypeElement> elements, final Class<?> clazz) {
        return !elements.isEmpty() &&
                elements.stream()
                        .map(TypeElement::getQualifiedName)
                        .anyMatch(fqName -> fqName.contentEquals(clazz.getName()));
    }

}

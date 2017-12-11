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

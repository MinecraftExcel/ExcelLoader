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

package me.jamiemansfield.excel.util;

import net.minecraft.util.ResourceLocation;

import java.util.Objects;

/**
 * Represents a namespace, providing convenience methods for a namespace,
 * supplementing {@link ResourceLocation}.
 */
public class Namespace implements Comparable<Namespace> {

    /**
     * A {@link Namespace} instance for Minecraft.
     */
    public static final Namespace MINECRAFT = new Namespace("minecraft");

    /**
     * A {@link Namespace} instance for ExcelLoader.
     */
    public static final Namespace EXCELLOADER = new Namespace("excelloader");

    protected final String namespace;

    /**
     * Creates a new namespace wrapper, using the given namespace.
     *
     * @param namespace The namespace to use
     */
    public Namespace(final String namespace) {
        this.namespace = namespace;
    }

    /**
     * Gets the namespace, the wrapper represents.
     *
     * @return The namespace
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Resolves a {@link ResourceLocation} for the given path and
     * the namespace.
     *
     * @param path The path to resolve
     * @return The resource location
     */
    public ResourceLocation resolve(final String path) {
        return new ResourceLocation(this.namespace, path);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Namespace)) return false;
        final Namespace that = (Namespace) obj;
        return Objects.equals(this.namespace, that.namespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.namespace);
    }

    @Override
    public int compareTo(final Namespace o) {
        return Objects.compare(this.namespace, o.namespace, String::compareTo);
    }

}

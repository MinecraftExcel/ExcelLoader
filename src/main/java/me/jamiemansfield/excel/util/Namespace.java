/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
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

/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.mod;

import me.jamiemansfield.excel.util.Namespace;

/**
 * A dummy implementation of {@link ModContainer} used internally by
 * ExcelLoader, for itself and Minecraft.
 */
public class DummyModContainer implements ModContainer {

    private final Namespace namespace;
    private final String name;
    private final String version;

    public DummyModContainer(final Namespace namespace, final String name, final String version) {
        this.namespace = namespace;
        this.name = name;
        this.version = version;
    }

    public DummyModContainer(final Namespace namespace, final String version) {
        this(namespace, namespace.getNamespace(), version);
    }

    @Override
    public Namespace getNamespace() {
        return this.namespace;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

}

/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod.system;

import me.jamiemansfield.excel.mod.ModContainer;
import me.jamiemansfield.excel.mod.ModDescriptor;
import me.jamiemansfield.excel.util.Namespace;

import java.util.Optional;

/**
 * The implementation of {@link ModContainer} for {@link ExcelModSystem}.
 */
class ExcelModContainer implements ModContainer {

    private final Namespace namespace;
    private final String name;
    private final String version;
    private final Object instance;

    ExcelModContainer(final ModDescriptor descriptor, final Object instance) {
        this.namespace = new Namespace(descriptor.getId());
        this.name = descriptor.getName();
        this.version = descriptor.getVersion();
        this.instance = instance;
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

    @Override
    public Optional<?> getInstance() {
        return Optional.of(this.instance);
    }

}

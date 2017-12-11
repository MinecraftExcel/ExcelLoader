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

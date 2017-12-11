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

package me.jamiemansfield.excel.mod;

import me.jamiemansfield.excel.util.Namespace;

import java.util.Optional;

/**
 * A container to represent a mod, and its associated metadata.
 */
public interface ModContainer {

    /**
     * Gets the namespace of which the mod has.
     *
     * @return The mod's namespace
     */
    Namespace getNamespace();

    /**
     * Gets the identifier of the mod being contained.
     *
     * @return The mod's identifier
     */
    default String getId() {
        return this.getNamespace().getNamespace();
    }

    /**
     * Gets the name of the mod being contained.
     *
     * @return The mod's name
     */
    default String getName() {
        return this.getId();
    }

    /**
     * Gets the version of the mod being contained.
     *
     * @return The mod's version
     */
    String getVersion();

    /**
     * Gets the instance of the mod being contained.
     *
     * @return The mod's instance
     */
    default Optional<?> getInstance() {
        return Optional.empty();
    }

}

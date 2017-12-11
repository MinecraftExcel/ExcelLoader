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

package me.jamiemansfield.excel.mod.descriptor;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import java.util.Objects;

/**
 * An immutable representation of a mod's descriptor, independent of format.
 */
public class ModDescriptor {

    private final String system;
    private final String id;
    private final String name;
    private final String version;

    public ModDescriptor(final String system, final String id, final String name,
            final String version) {
        this.system = system;
        this.id = id;
        this.name = Strings.isNullOrEmpty(name) ? id : name;
        this.version = Strings.isNullOrEmpty(version) ? "0.0.1" : version;
    }

    /**
     * Gets the modification system that will load the mod.
     *
     * @return The mod system
     */
    public final String getSystem() {
        return this.system;
    }

    /**
     * Gets the identifier of the mod being described.
     *
     * @return The mod's identifier
     */
    public final String getId() {
        return this.id;
    }

    /**
     * Gets the name of the mod being described.
     *
     * @return The mod's name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Gets the version of the mod being described.
     *
     * @return The mod's version
     */
    public final String getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.system, this.id, this.name, this.version);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("system", this.system)
                .add("id", this.id)
                .add("name", this.name)
                .add("version", this.version)
                .toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ModDescriptor)) return false;
        final ModDescriptor that = (ModDescriptor) obj;
        return Objects.equals(this.system, that.system) &&
                Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.version, that.version);
    }

}

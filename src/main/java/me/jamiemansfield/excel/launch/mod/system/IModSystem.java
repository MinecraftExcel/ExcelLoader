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

import me.jamiemansfield.excel.launch.mod.ModCandidate;
import me.jamiemansfield.excel.mod.ModContainer;
import me.jamiemansfield.excel.mod.descriptor.DescriptorFormat;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Represents the framework for a mod system.
 */
public interface IModSystem {

    /**
     * A proxy of {@link ITweaker#injectIntoClassLoader(LaunchClassLoader)}
     * to allow mod systems to do similar functionality to that from
     * {@link ITweaker}s.
     *
     * @param loader The class loader
     * @see ITweaker#injectIntoClassLoader(LaunchClassLoader)
     */
    default void injectIntoClassLoader(final LaunchClassLoader loader) {
    }

    /**
     * Provides the descriptor formats that the mod system uses.
     *
     * @return The descriptor formats, to register
     */
    default Collection<DescriptorFormat> provideDescriptorFormats() {
        return Collections.emptySet();
    }

    /**
     * Loads the given {@link ModCandidate}, and creates a mod container
     * for it.
     *
     * @param modCandidate The mod candidate
     * @return The mod container
     */
    Optional<ModContainer> load(final ModCandidate modCandidate);

}

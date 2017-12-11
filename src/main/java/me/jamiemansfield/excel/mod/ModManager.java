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

import com.google.common.collect.Maps;
import me.jamiemansfield.excel.SharedConstants;
import me.jamiemansfield.excel.util.Namespace;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * A manager for managing mods installed by the player.
 */
public class ModManager {

    public static final ModContainer MINECRAFT = new DummyModContainer(Namespace.MINECRAFT, "Minecraft", SharedConstants.Mc.VERSION);
    public static final ModContainer EXCELLOADER = new DummyModContainer(Namespace.EXCELLOADER, "ExcelLoader", SharedConstants.VERSION);

    private final Map<String, ModContainer> mods = Maps.newHashMap();

    /**
     * Gets an immutable {@link Collection} of all {@link ModContainer}s.
     *
     * @return The mods
     */
    public Collection<ModContainer> getMods() {
        return Collections.unmodifiableCollection(this.mods.values());
    }

    /**
     * Gets the {@link ModContainer} of the given mod identifier, given
     * it exists, wrapped in a {@link Optional}.
     *
     * @param id The identifier of the mod
     * @return The mod container
     */
    public Optional<ModContainer> getMod(final String id) {
        return Optional.ofNullable(this.mods.get(id));
    }

    /**
     * Checks if a mod of the given identifier has been loaded into the
     * mod manager.
     *
     * @param id The identifier of the mod
     * @return {@code True} if a mod of the given identifier has been loaded,
     *         {@code false} otherwise
     */
    public boolean isLoaded(final String id) {
        return this.mods.containsKey(id);
    }

}

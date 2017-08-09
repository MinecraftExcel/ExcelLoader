/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
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

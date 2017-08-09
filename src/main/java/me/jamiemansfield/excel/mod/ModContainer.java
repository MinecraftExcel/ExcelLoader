/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
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

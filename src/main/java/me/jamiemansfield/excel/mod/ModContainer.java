/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.mod;

/**
 * A container to represent a mod, and its associated metadata.
 */
public interface ModContainer {

    /**
     * Gets the identifier of the mod being contained.
     *
     * @return The mod's identifier
     */
    String getId();

    /**
     * Gets the name of the mod being contained.
     *
     * @return The mod's name
     */
    String getName();

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
    Object getMod();

}

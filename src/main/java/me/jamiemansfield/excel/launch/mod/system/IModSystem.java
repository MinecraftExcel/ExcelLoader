/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod.system;

import me.jamiemansfield.excel.launch.mod.ModCandidate;
import me.jamiemansfield.excel.mod.ModContainer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

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
     * Loads the given {@link ModCandidate}, and creates a mod container
     * for it.
     *
     * @param modCandidate The mod candidate
     * @return The mod container
     */
    Optional<ModContainer> load(final ModCandidate modCandidate);

}

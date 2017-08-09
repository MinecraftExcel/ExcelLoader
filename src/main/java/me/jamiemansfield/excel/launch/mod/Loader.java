/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod;

import com.google.common.collect.Maps;
import me.jamiemansfield.excel.launch.mod.system.ExcelModSystem;
import me.jamiemansfield.excel.launch.mod.system.IModSystem;

import java.util.Map;

/**
 * The actual mod loader of all this, that will handle reading mods
 * and passing them along to the appropriate mod system for creating
 * an instance of the mod.
 */
public final class Loader {

    /**
     * A {@link Map} for translating between mod system ids and the mod system
     * itself.
     */
    private static final Map<String, IModSystem> modSystems = Maps.newHashMap();

    static {
        // Register the "Excel" mod system
        modSystems.put("Excel", new ExcelModSystem());
    }

}

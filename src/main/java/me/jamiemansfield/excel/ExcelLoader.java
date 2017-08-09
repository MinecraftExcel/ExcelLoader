/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel;

import me.jamiemansfield.excel.mod.ModContainer;
import me.jamiemansfield.excel.mod.ModManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The 'brains of the operation'.
 */
public final class ExcelLoader {

    /**
     * The {@link Logger} used internally by ExcelLoader.
     *
     * <b>Mods should not used this {@link Logger}!</b>
     */
    public static final Logger log = LogManager.getLogger("ExcelLoader");

    /**
     * The manager for {@link ModContainer}s.
     */
    public static final ModManager modManager = new ModManager();

    private ExcelLoader() {
    }

}

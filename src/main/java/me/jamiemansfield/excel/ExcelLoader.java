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

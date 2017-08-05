/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel;

/**
 * A collection of constants used throughout ExcelLoader.
 */
public final class SharedConstants {

    /**
     * The version of ExcelLoader.
     */
    public static final String VERSION = "0.0.1-indev";

    private SharedConstants() {
    }

    /**
     * A collection of constants used throughout ExcelLoader, pertaining
     * to Minecraft.
     */
    public static final class Mc {

        /**
         * The version of Minecraft.
         */
        public static final String VERSION = "1.12";

        /**
         * The protocol version for the version of Minecraft.
         */
        public static final int PROTOCOL_VERSION = 335;

        private Mc() {
        }

    }

}

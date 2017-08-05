/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a class as being the main class for a mod. The annotation
 * holds an identifier, alongside optional metadata, for a mod. This
 * information is provided to ExcelLoader through use of a bundled
 * <code>mod.json</code> file distributed alongside the mod.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {

    /**
     * Gets the identifier for the mod. The identifier will be used
     * to identify the mod, so it should be unique!
     *
     * @return The mod identifier
     * @see ModDescriptor#getId()
     */
    String id();

    /**
     * Gets the name of the mod. This will default to the mod identifier
     * if no name is specified.
     *
     * @return The mod name
     * @see ModDescriptor#getName()
     */
    String name() default "";

    /**
     * Gets the version of the mod. This will default to <code>0.0.1</code>
     * if not specified.
     *
     * @return The mod version
     * @see ModDescriptor#getVersion()
     */
    String version() default "0.0.1";

}

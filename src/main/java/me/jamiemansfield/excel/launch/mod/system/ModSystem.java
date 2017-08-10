/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a class as being an ExcelLoader mod system. The annotation
 * simply only needs to be given the identifier given to the mod system.
 * This information is then obtained by ExcelLoader through the mod system's
 * bundled <pre>mod-system.properties</pre> file.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModSystem {

    /**
     * Gets the identifier for the mod system. The identifier will be used
     * to identify the mod system, so it should be UNIQUE!
     *
     * @return The mod system identifier
     */
    String value();

}

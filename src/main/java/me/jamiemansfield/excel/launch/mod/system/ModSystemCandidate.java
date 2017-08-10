/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod.system;

import java.nio.file.Path;

public class ModSystemCandidate {

    private final Path source;
    private final String id;
    private final String mainClass;

    public ModSystemCandidate(final Path source, final String id, final String mainClass) {
        this.source = source;
        this.id = id;
        this.mainClass = mainClass;
    }

    public final Path getSource() {
        return this.source;
    }

    public final String getId() {
        return this.id;
    }

    public final String getMainClass() {
        return this.mainClass;
    }

}

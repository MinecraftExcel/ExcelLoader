/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod;

import me.jamiemansfield.excel.mod.ModDescriptor;

import java.nio.file.Path;

public class ModCandidate {

    private final Path source;
    private final ModDescriptor descriptor;

    public ModCandidate(final Path source, final ModDescriptor descriptor) {
        this.source = source;
        this.descriptor = descriptor;
    }

    public final Path getSource() {
        return this.source;
    }

    public final ModDescriptor getDescriptor() {
        return this.descriptor;
    }

}

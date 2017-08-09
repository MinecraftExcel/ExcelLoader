/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod.system;

import com.google.inject.AbstractModule;
import me.jamiemansfield.excel.mod.ModDescriptor;

class ExcelModGuiceModule extends AbstractModule {

    private final ModDescriptor descriptor;

    ExcelModGuiceModule(final ModDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    protected void configure() {
    }

}

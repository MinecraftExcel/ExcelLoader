/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch;

import me.jamiemansfield.excel.SharedConstants;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.List;

/**
 * The client tweaker class for the ExcelLoader modification loader.
 */
public class ExcelLoaderClientTweaker extends ExcelLoaderTweaker {

    @Override
    public void _acceptOptions(final List<String> args, final File gameDir, final File assetsDir,
            final String profile) {
        // Populate the launch arguments with any missing *mandatory* arguments
        if (!this.launchArgs.containsKey("--version")) {
            this.launchArgs.put("--version", SharedConstants.Mc.VERSION);
        }
        if (!this.launchArgs.containsKey("--gameDir")) {
            this.launchArgs.put("--gameDir", gameDir != null ? gameDir.getAbsolutePath() : new File(".").getAbsolutePath());
        }
        if (!this.launchArgs.containsKey("--assetsDir") && assetsDir != null) {
            this.launchArgs.put("--assetsDir", assetsDir.getAbsolutePath());
        }
    }

    @Override
    public String getLaunchTarget() {
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        return "net.minecraft.client.main.Main";
    }

}

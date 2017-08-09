/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch;

import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.List;

/**
 * The server tweaker class for the ExcelLoader modification loader.
 */
public class ExcelLoaderServerTweaker extends ExcelLoaderTweaker {

    @Override
    public void _acceptOptions(final List<String> args, final File gameDir, final File assetsDir,
            final String profile) {
    }

    @Override
    public String getLaunchTarget() {
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.SERVER);
        return "net.minecraft.server.MinecraftServer";
    }

}

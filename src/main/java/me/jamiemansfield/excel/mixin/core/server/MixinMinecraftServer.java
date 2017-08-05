/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.mixin.core.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    /**
     * @author jamierocks - 2nd August 2017
     * @reason Set the server brand appropriately
     */
    @Overwrite
    public String getServerModName() {
        return "excelloader";
    }

}

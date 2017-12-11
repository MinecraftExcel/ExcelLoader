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

package me.jamiemansfield.excel.launch;

import static me.jamiemansfield.excel.ExcelLoader.log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.jamiemansfield.excel.SharedConstants;
import me.jamiemansfield.excel.launch.mod.Loader;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * The tweaker class for the ExcelLoader modification loader.
 */
public abstract class ExcelLoaderTweaker implements ITweaker {

    protected Map<String, String> launchArgs;
    protected List<String> hangingArgs;
    protected File gameDir;

    @Override
    public void acceptOptions(final List<String> args, final File gameDir, final File assetsDir,
            final String profile) {
        // Store game directory for use later on
        this.gameDir = gameDir != null ? gameDir : new File(".");

        // Use the launch arguments on Launch.blackboard, so the full args are passed between loaders
        this.launchArgs = (Map<String, String>) Launch.blackboard.get("launchArgs");
        if (this.launchArgs == null) {
            Launch.blackboard.put("launchArgs", this.launchArgs = Maps.newHashMap());
        }
        this.hangingArgs = Lists.newArrayList();

        // Populate the launch arguments from the arguments given to ExcelLoader
        String classifier = null;
        for (final String arg : args) {
            // Check if the argument is a classifier
            if (arg.startsWith("-")) {
                // First check if there is an active classifier, and if so, leave a blank entry for it
                if (classifier != null) {
                    this.launchArgs.put(classifier, "");
                }
                // Now check if the argument is a painful one, using a equals sign between the classifier and entry
                else if (arg.contains("=")) {
                    final String[] argument = arg.split("=", 2);
                    this.launchArgs.put(argument[0], argument[1]);
                }
                // Well, it is a classifier
                else {
                    classifier = arg;
                }
            }
            // Its not a classifier, so its a value
            else {
                // First check if there is an active classifier, and if so, place the entry for it
                if (classifier != null) {
                    this.launchArgs.put(classifier, arg);
                    classifier = null;
                }
                // The argument is a hanging argument (has no classifier)
                else {
                    hangingArgs.add(arg);
                }
            }
        }

        // Let the server/client tweaker do what it wants
        this._acceptOptions(args, gameDir, assetsDir, profile);
    }

    public abstract void _acceptOptions(final List<String> args, final File gameDir, final File assetsDir,
            final String profile);

    @Override
    public void injectIntoClassLoader(final LaunchClassLoader loader) {
        log.info("Initialising ExcelLoader v{}", SharedConstants.VERSION);

        configureLaunchClassLoader(loader);
        configureMixinEnvironment();
        Loader.init(this.gameDir.toPath());
        Loader.loadModSystems(loader);
        Loader.loadMods(loader);

        log.info("Initialisation complete. Starting Minecraft {}...", SharedConstants.Mc.VERSION);
    }

    @Override
    public String[] getLaunchArguments() {
        final List<String> args = Lists.newArrayList();
        args.addAll(this.hangingArgs);

        this.launchArgs.forEach((classifier, value) -> {
            args.add(classifier.trim());
            args.add(value.trim());
        });

        this.hangingArgs.clear();
        this.launchArgs.clear();

        return args.toArray(new String[args.size()]);
    }

    private static void configureLaunchClassLoader(final LaunchClassLoader loader) {
        // Logging
        loader.addClassLoaderExclusion("com.mojang.util.QueueLogAppender");

        // ExcelLoader launch code
        loader.addClassLoaderExclusion("com.google.common.");
        loader.addClassLoaderExclusion("me.jamiemansfield.excel.launch.");
        loader.addClassLoaderExclusion("me.jamiemansfield.excel.ExcelLoader");
    }

    private static void configureMixinEnvironment() {
        log.debug("Initialising Mixin environment...");
        MixinBootstrap.init();
        Mixins.addConfigurations(
                "mixins.excelloader.core.json");
    }

}

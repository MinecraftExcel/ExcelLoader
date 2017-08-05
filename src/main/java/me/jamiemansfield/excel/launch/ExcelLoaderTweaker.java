/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch;

import static me.jamiemansfield.excel.ExcelLoader.log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.jamiemansfield.excel.SharedConstants;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * The tweaker class for the ExcelLoader modification loader.
 */
public class ExcelLoaderTweaker implements ITweaker {

    private Map<String, String> launchArgs;
    private List<String> hangingArgs;

    @Override
    public void acceptOptions(final List<String> args, final File gameDir, final File assetsDir,
            final String profile) {
        // Use the launch arguments on Launch.blackboard, so the full args are passed between loaders
        this.launchArgs = (Map<String, String>) Launch.blackboard.get("launchArgs");
        if (this.launchArgs == null) Launch.blackboard.put("launchArgs", this.launchArgs = Maps.newHashMap());
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
    public void injectIntoClassLoader(final LaunchClassLoader loader) {
        log.info("Initialising ExcelLoader v{}", SharedConstants.VERSION);

        configureLaunchClassLoader(loader);
        configureMixinEnvironment();

        log.info("Initialisation complete. Starting Minecraft...");
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
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
    }

    private static void configureMixinEnvironment() {
        log.debug("Initialising Mixin environment...");
        MixinBootstrap.init();
        Mixins.addConfigurations(
                "mixins.excelloader.core.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

}
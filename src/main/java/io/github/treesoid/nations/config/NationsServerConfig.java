package io.github.treesoid.nations.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.treesoid.nations.helper.ConfigHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class NationsServerConfig {
    public static NationsConfigObject CONFIG;
    public static boolean loaded = false;

    public static void load() {
        CONFIG = ConfigHelper.load(configPath(), NationsConfigObject::new);
        loaded = true;
    }

    public static void save() {
        ConfigHelper.save(configPath(), CONFIG, NationsServerConfig::applyComments);
    }

    private static void applyComments(CommentedFileConfig config) {
        config.setComment("abilities.fartJump.verticalVelocity", " Amount of vertical velocity the player recives when activating.");
        config.setComment("abilities.fartJump.verticalVelocityLarge", " Amount of vertical velocity the player recives when the fart is large.");
        config.setComment("abilities.fartJump.verticalVelocityExplosive", " Amount of vertical velocity the player recives when the fart is explosive.");

        config.setComment("abilities.fartJump.largeChance", " Chance for a large fart, 0=0%, 1=100%.");
        config.setComment("abilities.fartJump.explosiveChance", " Chance for an explosive fart, 0=0%, 1=100%.");

        config.setComment("abilities.dash.factor", " How much velocity the player recives when activating.");
        config.setComment("abilities.dash.verticalFactor", " Vertical velocity multiplier (additional).");
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("nations").resolve("server.conf");
    }
}

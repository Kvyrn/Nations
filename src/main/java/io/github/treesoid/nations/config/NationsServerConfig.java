package io.github.treesoid.nations.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.treesoid.nations.helper.ConfigHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class NationsServerConfig {
    public static NationsConfigObject CONFIG;

    public static void load() {
        CONFIG = ConfigHelper.load(configPath(), NationsConfigObject::new);
    }

    public static void save() {
        ConfigHelper.save(configPath(), CONFIG, NationsServerConfig::applyComments);
    }

    private static void applyComments(CommentedFileConfig config) {
        config.setComment("abilities.fartJump.verticalVelocity", "Amount of vertical velocity the player recives when activating.");
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("nations").resolve("server.conf");
    }
}

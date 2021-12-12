package io.github.treesoid.nations.client.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.treesoid.nations.helper.ConfigHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class NationsClientConfig {
    public static NationsClientConfigObject CONFIG;

    public static void load() {
        CONFIG = ConfigHelper.load(configPath(), NationsClientConfigObject::new);
    }

    public static void save() {
        ConfigHelper.save(configPath(), CONFIG, NationsClientConfig::applyComments);
    }

    private static void applyComments(CommentedFileConfig config) {
        config.setComment("cycleToSelect", """
                Changes ability selections behaviour.
                If enabled, pressing the ability select
                key cycles between favourite abilities
                instead of showing a menu.
                """.indent(1));
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("nations").resolve("client.conf");
    }
}

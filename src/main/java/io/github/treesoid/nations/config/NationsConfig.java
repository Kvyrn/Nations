package io.github.treesoid.nations.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class NationsConfig {
    public static NationsConfigObject SERVER_CONFIG;
    public static final ObjectConverter OBJECT_CONVERTER = new ObjectConverter();

    public static void load() {
        File file = configPath().toFile();
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdir();
        }
        CommentedFileConfig configObject = createConfig();
        configObject.load();
        tryFixConfig(configObject);
        SERVER_CONFIG = OBJECT_CONVERTER.toObject(configObject, NationsConfigObject::new);
        configObject.close();
    }

    public static void save() {
        CommentedFileConfig configObject = OBJECT_CONVERTER.toConfig(SERVER_CONFIG, NationsConfig::createConfig);
        applyComments(configObject);
        configObject.save();
        configObject.close();
    }

    private static CommentedFileConfig createConfig() {
        return CommentedFileConfig.builder(configPath())
                .preserveInsertionOrder()
                .build();
    }

    private static void applyComments(CommentedFileConfig config) {
        config.setComment("abilities.fartJump.verticalVelocity", "Amount of vertical velocity the player recives when activating.");
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("nations").resolve("server.conf");
    }

    private static void tryFixConfig(CommentedFileConfig config) {
        CommentedFileConfig defConfig = OBJECT_CONVERTER.toConfig(new NationsConfigObject(), NationsConfig::createConfig);
        fixInternal(config, defConfig);
    }

    private static void fixInternal(Config input, Config def) {
        Map<String, Object> map = def.valueMap();
        for (String entry : map.keySet()) {
            Object obj = map.get(entry);
            if (!input.contains(entry)) {
                input.set(entry, obj);
            }
            if (obj instanceof Config) {
                fixInternal(input.get(entry), def.get(entry));
            }
        }
    }
}

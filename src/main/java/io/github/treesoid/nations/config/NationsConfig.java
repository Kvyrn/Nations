package io.github.treesoid.nations.config;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.IndentStyle;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlWriter;
import io.github.treesoid.nations.Nations;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class NationsConfig {
    public static NationsConfigObject CONFIG;
    public static final ObjectConverter OBJECT_CONVERTER = new ObjectConverter();
    public static final TomlWriter writer = TomlFormat.instance().createWriter();
    static {
        writer.setIndent(IndentStyle.SPACES_4);
    }

    public static void load() {
        File file = configPath().toFile();
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    file.getParentFile().mkdir();
                }
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                FileConfig defConfig = OBJECT_CONVERTER.toConfig(new NationsConfigObject(), NationsConfig::createConfig);
                writer.write(defConfig, file, WritingMode.REPLACE);
                defConfig.close();
            } catch (IOException e) {
                Nations.LOGGER.error("[Nations] Failed to create config file! (" + file.getAbsolutePath() + ")", e);
            }
        }
        FileConfig configObject = createConfig();
        configObject.load();
        CONFIG = OBJECT_CONVERTER.toObject(configObject, NationsConfigObject::new);
        configObject.close();
    }

    public static void save() {
        FileConfig configObject = OBJECT_CONVERTER.toConfig(CONFIG, NationsConfig::createConfig);
        writer.write(configObject, configObject.getFile(), WritingMode.REPLACE);
        configObject.close();
    }

    private static FileConfig createConfig() {
        return FileConfig.builder(configPath())
                .preserveInsertionOrder()
                .build();
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("nations").resolve("server.toml");
    }
}

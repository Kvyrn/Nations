package io.github.treesoid.nations;

import io.github.treesoid.nations.abilities.FartJumpAbility;
import io.github.treesoid.nations.abilities.util.Ability;
import io.github.treesoid.nations.commands.AbilityCommand;
import io.github.treesoid.nations.commands.argument.AbilityArgumentType;
import io.github.treesoid.nations.config.NationsConfig;
import io.github.treesoid.nations.database.IDatabaseHandler;
import io.github.treesoid.nations.database.MySQLDatabaseHandler;
import io.github.treesoid.nations.network.c2s.ActivateAbilityPacket;
import io.github.treesoid.nations.network.c2s.SelectAbilityPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.impl.gui.FabricGuiEntry;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Nations implements ModInitializer {
    public static final String modid = "nations";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final HashMap<Identifier, Ability> ABILITY_REGISTRY = new HashMap<>();
    public static IDatabaseHandler DATABASE_HANDLER;

    @Override
    public void onInitialize() {
        NationsConfig.load();
        NationsConfig.save();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + NationsConfig.CONFIG.database.url, NationsConfig.CONFIG.database.username, NationsConfig.CONFIG.database.password);
            DATABASE_HANDLER = new MySQLDatabaseHandler(connection);
        } catch (SQLException e) {
            FabricGuiEntry.displayCriticalError(e, true);
        }

        //noinspection CodeBlock2Expr
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            AbilityCommand.register(dispatcher);
        });

        ABILITY_REGISTRY.put(FartJumpAbility.IDENTIFIER, FartJumpAbility.INSTANCE);

        ArgumentTypes.register(modid + ":ability", AbilityArgumentType.class, new ConstantArgumentSerializer<>(AbilityArgumentType::ability));

        registerNetworkRecivers();
    }

    public void registerNetworkRecivers() {
        ActivateAbilityPacket.registerReciver();
        SelectAbilityPacket.registerReciver();
    }
}

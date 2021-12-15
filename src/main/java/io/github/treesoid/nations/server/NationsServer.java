package io.github.treesoid.nations.server;

import io.github.treesoid.nations.config.NationsServerConfig;
import io.github.treesoid.nations.database.IDatabaseHandler;
import io.github.treesoid.nations.database.MySQLDatabaseHandler;
import io.github.treesoid.nations.network.c2s.ActivateAbilityPacket;
import io.github.treesoid.nations.network.c2s.SelectAbilityPacket;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.impl.gui.FabricGuiEntry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class NationsServer implements DedicatedServerModInitializer {
    public static IDatabaseHandler DATABASE_HANDLER;
    public static final HashMap<Block, Integer> RESOURCE_POINTS = new HashMap<>();

    public static int getResourcePointsForBlock(Block block) {
        return RESOURCE_POINTS.getOrDefault(block, 0);
    }

    @Override
    public void onInitializeServer() {
        NationsServerConfig.load();
        NationsServerConfig.save();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + NationsServerConfig.CONFIG.database.url, NationsServerConfig.CONFIG.database.username, NationsServerConfig.CONFIG.database.password);
            DATABASE_HANDLER = new MySQLDatabaseHandler(connection);
        } catch (SQLException e) {
            FabricGuiEntry.displayCriticalError(e, true);
        }

        registerNetworkRecivers();
    }

    public void registerNetworkRecivers() {
        ActivateAbilityPacket.registerReciver();
        SelectAbilityPacket.registerReciver();
    }

    static {
        RESOURCE_POINTS.put(Blocks.COAL_ORE, 2);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_COAL_ORE, 2);
        RESOURCE_POINTS.put(Blocks.IRON_ORE, 5);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_IRON_ORE, 5);
        RESOURCE_POINTS.put(Blocks.GOLD_ORE, 6);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_GOLD_ORE, 6);
        RESOURCE_POINTS.put(Blocks.COPPER_ORE, 3);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_COPPER_ORE, 3);
        RESOURCE_POINTS.put(Blocks.REDSTONE_ORE, 10);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_REDSTONE_ORE, 10);
        RESOURCE_POINTS.put(Blocks.LAPIS_ORE, 10);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_LAPIS_ORE, 10);
        RESOURCE_POINTS.put(Blocks.DIAMOND_ORE, 20);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_DIAMOND_ORE, 20);
        RESOURCE_POINTS.put(Blocks.EMERALD_ORE, 25);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_EMERALD_ORE, 25);
    }
}

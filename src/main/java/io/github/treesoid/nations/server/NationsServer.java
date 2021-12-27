package io.github.treesoid.nations.server;

import io.github.treesoid.nations.config.NationsServerConfig;
import io.github.treesoid.nations.database.IDatabaseHandler;
import io.github.treesoid.nations.database.MySQLDatabaseHandler;
import io.github.treesoid.nations.network.c2s.ActivateAbilityPacket;
import io.github.treesoid.nations.network.c2s.RequestSyncAbilityListPacket;
import io.github.treesoid.nations.network.c2s.SelectAbilityPacket;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.impl.gui.FabricGuiEntry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import static io.github.treesoid.nations.config.NationsServerConfig.CONFIG;

public class NationsServer implements DedicatedServerModInitializer {
    public static IDatabaseHandler DATABASE_HANDLER;
    public static final HashMap<Block, Integer> RESOURCE_POINTS = new HashMap<>();

    public static int getResourcePointsForBlock(Block block, int fortuneLevel) {
        int base = RESOURCE_POINTS.getOrDefault(block, 0);
        return (int) Math.round(switch (fortuneLevel) {
            case 1 -> base * CONFIG.resourcePoints.fortune1Multiplier;
            case 2 -> base * CONFIG.resourcePoints.fortune2Multiplier;
            case 3 -> base * CONFIG.resourcePoints.fortune3Multiplier;
            default -> base;
        });
    }

    @Override
    public void onInitializeServer() {
        NationsServerConfig.load();
        NationsServerConfig.save();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + CONFIG.database.url, CONFIG.database.username, CONFIG.database.password);
            DATABASE_HANDLER = new MySQLDatabaseHandler(connection);
        } catch (SQLException e) {
            FabricGuiEntry.displayCriticalError(e, true);
        }

        registerNetworkRecivers();

        RESOURCE_POINTS.put(Blocks.COAL_ORE, CONFIG.resourcePoints.coalOre);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_COAL_ORE, CONFIG.resourcePoints.coalOre);
        RESOURCE_POINTS.put(Blocks.IRON_ORE, CONFIG.resourcePoints.ironOre);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_IRON_ORE, CONFIG.resourcePoints.ironOre);
        RESOURCE_POINTS.put(Blocks.GOLD_ORE, CONFIG.resourcePoints.goldOre);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_GOLD_ORE, CONFIG.resourcePoints.goldOre);
        RESOURCE_POINTS.put(Blocks.COPPER_ORE, CONFIG.resourcePoints.copperOre);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_COPPER_ORE, CONFIG.resourcePoints.copperOre);
        RESOURCE_POINTS.put(Blocks.REDSTONE_ORE, CONFIG.resourcePoints.redstoneOre);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_REDSTONE_ORE, CONFIG.resourcePoints.redstoneOre);
        RESOURCE_POINTS.put(Blocks.LAPIS_ORE, CONFIG.resourcePoints.lapisOre);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_LAPIS_ORE, CONFIG.resourcePoints.lapisOre);
        RESOURCE_POINTS.put(Blocks.DIAMOND_ORE, CONFIG.resourcePoints.diamondOre);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_DIAMOND_ORE, CONFIG.resourcePoints.diamondOre);
        RESOURCE_POINTS.put(Blocks.EMERALD_ORE, CONFIG.resourcePoints.emeraldOre);
        RESOURCE_POINTS.put(Blocks.DEEPSLATE_EMERALD_ORE, CONFIG.resourcePoints.emeraldOre);
    }

    public void registerNetworkRecivers() {
        ActivateAbilityPacket.registerReciver();
        SelectAbilityPacket.registerReciver();
        RequestSyncAbilityListPacket.registerReciver();
    }
}

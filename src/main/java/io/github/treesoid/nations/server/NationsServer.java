package io.github.treesoid.nations.server;

import io.github.treesoid.nations.config.NationsServerConfig;
import io.github.treesoid.nations.database.IDatabaseHandler;
import io.github.treesoid.nations.database.MySQLDatabaseHandler;
import io.github.treesoid.nations.network.c2s.ActivateAbilityPacket;
import io.github.treesoid.nations.network.c2s.SelectAbilityPacket;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.impl.gui.FabricGuiEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NationsServer implements DedicatedServerModInitializer {
    public static IDatabaseHandler DATABASE_HANDLER;

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
}

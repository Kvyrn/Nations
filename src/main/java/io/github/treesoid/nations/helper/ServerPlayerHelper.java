package io.github.treesoid.nations.helper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class ServerPlayerHelper {
    private ServerPlayerHelper() {
    }

    public static boolean playerOnline(UUID uuid, MinecraftServer server) {
        return server.getPlayerManager().getPlayer(uuid) != null;
    }

    public static ServerPlayerEntity getPlayer(UUID uuid, MinecraftServer server) {
        return server.getPlayerManager().getPlayer(uuid);
    }
}

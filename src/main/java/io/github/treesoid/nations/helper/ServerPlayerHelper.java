package io.github.treesoid.nations.helper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class ServerPlayerHelper {
    private ServerPlayerHelper() {}

    public static boolean playerOnline(UUID uuid, MinecraftServer server) {
        return server.getPlayerManager().getPlayer(uuid) != null;
    }

    public static PlayerEntity getPlayer(UUID uuid, MinecraftServer server) {
        return server.getPlayerManager().getPlayer(uuid);
    }
}

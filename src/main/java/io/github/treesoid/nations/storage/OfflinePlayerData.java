package io.github.treesoid.nations.storage;

import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class OfflinePlayerData extends PlayerData {
    public OfflinePlayerData(UUID uuid, String name) {
        super(uuid, name);
    }

    @Override
    PlayerEntity getPlayer() {
        return null;
    }
}

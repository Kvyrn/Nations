package io.github.treesoid.nations.storage;

import net.minecraft.entity.player.PlayerEntity;

public class OnlinePlayerData  extends PlayerData {
    public final PlayerEntity player;

    public OnlinePlayerData(PlayerEntity player, String name) {
        super(player, name);
        this.player = player;
    }

    @Override
    PlayerEntity getPlayer() {
        return player;
    }
}

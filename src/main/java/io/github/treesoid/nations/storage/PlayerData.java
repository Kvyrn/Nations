package io.github.treesoid.nations.storage;

import io.github.treesoid.nations.abilities.util.Ability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public abstract class PlayerData {
    public final UUID uuid;
    public String name;
    public long playtime;
    public Identifier nation;
    public int resourcePoints;
    public Ability selectedAbility;

    protected PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    protected PlayerData(PlayerEntity player, String name) {
        uuid = player.getUuid();
        this.name = name;
    }

    abstract PlayerEntity getPlayer();

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public void setNation(Identifier nation) {
        this.nation = nation;
    }

    public void setResourcePoints(int resourcePoints) {
        this.resourcePoints = resourcePoints;
    }

    public void addResourcePoints(int points) {
        this.resourcePoints += points;
    }

    public void setSelectedAbility(Ability selectedAbility) {
        this.selectedAbility = selectedAbility;
    }
}

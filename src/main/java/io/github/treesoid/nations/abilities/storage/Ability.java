package io.github.treesoid.nations.abilities.storage;

import io.github.treesoid.nations.abilities.player.PlayerAbility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class Ability {
    public final List<UUID> holders = new LinkedList<>();
    public final Identifier identifier;

    public Ability(Identifier identifier) {
        this.identifier = identifier;
    }

    public abstract boolean canObtain(PlayerEntity  player);

    public void onUse(PlayerAbility ability) {
        ability.setCooldown(getMaxCooldown());
    }

    public abstract int getMaxCooldown();

    public boolean canUse(PlayerAbility ability) {
        return !ability.hasCooldown();
    }

    @Environment(EnvType.CLIENT)
    public abstract Identifier getIcon();

    public boolean matches(Ability ability) {
        return identifier.equals(ability.identifier);
    }

    public boolean matches(PlayerAbility ability) {
        return identifier.equals(ability.ability.identifier);
    }

    public abstract void onTrigger(PlayerAbility ability);
}

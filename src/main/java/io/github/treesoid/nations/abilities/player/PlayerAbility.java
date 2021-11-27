package io.github.treesoid.nations.abilities.player;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.storage.Ability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class PlayerAbility {
    public final PlayerEntity holder;
    protected int cooldown = 0;
    public final Ability ability;

    public PlayerAbility(PlayerEntity holder, Ability ability) {
        this.holder = holder;
        this.ability = ability;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean hasCooldown() {
        return cooldown < 1;
    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }

    public NbtCompound serialize() {
        NbtCompound compound = new NbtCompound();
        compound.putString("id", ability.identifier.toString());
        compound.putInt("cooldown", cooldown);
        return compound;
    }

    public static PlayerAbility deserialize(NbtCompound compound, PlayerEntity player) {
        Identifier abilityId = new Identifier(compound.getString("id"));
        PlayerAbility object = new PlayerAbility(player, Nations.ABILITY_REGISTRY.get(abilityId));
        object.setCooldown(compound.getInt("cooldown"));
        return object;
    }
}

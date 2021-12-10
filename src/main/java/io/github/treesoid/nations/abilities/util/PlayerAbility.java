package io.github.treesoid.nations.abilities.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

public class PlayerAbility {
    @Nullable
    public final PlayerEntity holder;
    public final Ability ability;
    protected int cooldown = 0;
    protected boolean favourite = false;

    public PlayerAbility(@Nullable PlayerEntity holder, Ability ability) {
        this.holder = holder;
        this.ability = ability;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean hasCooldown() {
        return cooldown < 1;
    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }

    public void use() {
        if (holder != null) {
            this.ability.onUse(this);
        }
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public NbtCompound serialize() {
        NbtCompound compound = new NbtCompound();
        compound.putString("id", ability.identifier.toString());
        compound.putInt("cooldown", cooldown);
        compound.putBoolean("favourite", favourite);
        return compound;
    }
}

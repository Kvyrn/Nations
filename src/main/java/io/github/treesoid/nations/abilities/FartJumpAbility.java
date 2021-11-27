package io.github.treesoid.nations.abilities;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.player.PlayerAbility;
import io.github.treesoid.nations.abilities.storage.Ability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class FartJumpAbility extends Ability {
    public static final Identifier IDENTIFIER = new Identifier(Nations.modid, "fart_jump");

    public FartJumpAbility() {
        super(IDENTIFIER);
    }

    @Override
    public boolean canObtain(PlayerEntity player) {
        return holders.size() < 10;
    }

    @Override
    public int getMaxCooldown() {
        return 100;
    }

    @Override
    public Identifier getIcon() {
        return new Identifier(Nations.modid, "textures/ability/fart_jump.png");
    }

    @Override
    public void onTrigger(PlayerAbility ability) {
    }
}

package io.github.treesoid.nations.abilities;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.util.PlayerAbility;
import io.github.treesoid.nations.abilities.util.Ability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class FartJumpAbility extends Ability {
    public static final Identifier IDENTIFIER = new Identifier(Nations.modid, "fart_jump");
    public static final FartJumpAbility INSTANCE = new FartJumpAbility();

    public FartJumpAbility() {
        super(IDENTIFIER);
    }

    @Override
    public boolean canObtain(PlayerEntity player) {
        return Nations.DATABASE_HANDLER.listHoldersOfAbility(this).size() < 10;
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
        ability.holder.sendMessage(new LiteralText("pffffffff"), false);
        ability.holder.addVelocity(0, 10, 0);
    }
}

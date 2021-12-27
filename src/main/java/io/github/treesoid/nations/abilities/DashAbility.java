package io.github.treesoid.nations.abilities;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.util.Ability;
import io.github.treesoid.nations.abilities.util.PlayerAbility;
import io.github.treesoid.nations.config.NationsServerConfig;
import io.github.treesoid.nations.network.s2c.AddVelocityPacket;
import io.github.treesoid.nations.server.NationsServer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class DashAbility extends Ability {
    public static final Identifier IDENTIFIER = new Identifier(Nations.modid, "dash");
    public static final DashAbility INSTANCE = new DashAbility();

    public DashAbility() {
        super(IDENTIFIER);
    }

    @Override
    public boolean canObtain(PlayerEntity player) {
        return NationsServer.DATABASE_HANDLER.listHoldersOfAbility(this).size() < 10;
    }

    @Override
    public int getMaxCooldown() {
        return 200;
    }

    @Override
    public Identifier getIcon() {
        return new Identifier(Nations.modid, "textures/ability/dash.png");
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onTrigger(@NotNull PlayerAbility ability) {
        // Multiply to copy
        ability.holder.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 3));
        Vec3d lookDirection = ability.holder.getRotationVector().multiply(1);
        lookDirection = lookDirection.normalize();
        lookDirection = lookDirection.multiply(NationsServerConfig.CONFIG.abilities.dash.factor);
        lookDirection = lookDirection.multiply(1, NationsServerConfig.CONFIG.abilities.dash.verticalFactor, 1);
        AddVelocityPacket.send((ServerPlayerEntity) ability.holder, lookDirection);
    }
}

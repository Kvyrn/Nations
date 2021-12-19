package io.github.treesoid.nations.abilities;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.NationsSounds;
import io.github.treesoid.nations.abilities.util.PlayerAbility;
import io.github.treesoid.nations.abilities.util.Ability;
import io.github.treesoid.nations.config.NationsServerConfig;
import io.github.treesoid.nations.network.s2c.AddVelocityPacket;
import io.github.treesoid.nations.server.NationsServer;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class FartJumpAbility extends Ability {
    public static final Identifier IDENTIFIER = new Identifier(Nations.modid, "fart_jump");
    public static final FartJumpAbility INSTANCE = new FartJumpAbility();

    public FartJumpAbility() {
        super(IDENTIFIER);
    }

    @Override
    public boolean canObtain(PlayerEntity player) {
        return NationsServer.DATABASE_HANDLER.listHoldersOfAbility(this).size() < 10;
    }

    @Override
    public int getMaxCooldown() {
        return 100;
    }

    @Override
    public Identifier getIcon() {
        return new Identifier(Nations.modid, "textures/ability/fart_jump.png");
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onTrigger(PlayerAbility ability) {
        Type type = getType(ability.holder.getRandom());
        ability.holder.sendMessage(new LiteralText("pffffffff " + type.name()), false);
        Vec3d pos = ability.holder.getPos();
        ability.holder.world.playSound(null, pos.x, pos.y, pos.z, type.sound, SoundCategory.PLAYERS, 2f, ability.holder.getRandom().nextFloat(0.5f, 1.5f));
        if (type == Type.EXPLOSIVE && !ability.holder.world.isClient) {
            ((ServerWorld)(ability.holder.world)).spawnParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x, pos.y, pos.z, 1, 1, 0, 0, 1);
            ability.holder.world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.5f, ability.holder.getRandom().nextFloat(0.5f, 1.5f));
        }
        AddVelocityPacket.send((ServerPlayerEntity) ability.holder, 0, velocityForType(type), 0);
    }

    private double velocityForType(Type type) {
        return switch (type) {
            case LARGE -> NationsServerConfig.CONFIG.abilities.fartJump.verticalVelocityLarge;
            case EXPLOSIVE -> NationsServerConfig.CONFIG.abilities.fartJump.verticalVelocityExplosive;
            default -> NationsServerConfig.CONFIG.abilities.fartJump.verticalVelocity;
        };
    }

    private Type getType(Random random) {
        if (random.nextDouble(1d) < NationsServerConfig.CONFIG.abilities.fartJump.largeChance) {
            return Type.LARGE;
        } else if (random.nextDouble(1d) < NationsServerConfig.CONFIG.abilities.fartJump.explosiveChance) {
            return Type.EXPLOSIVE;
        } else {
            return Type.NORMAL;
        }
    }

    private enum Type {
        NORMAL(NationsSounds.SHORT_FART),
        LARGE(NationsSounds.FART),
        EXPLOSIVE(NationsSounds.FART);

        public final SoundEvent sound;

        Type(SoundEvent sound) {
            this.sound = sound;
        }
    }
}

package io.github.treesoid.nations.mixin;

import io.github.treesoid.nations.Nations;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow protected abstract void takeShieldHit(LivingEntity attacker);

    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        Nations.DATABASE_HANDLER.getOrCreatePlayerAbilityList(this.getUuid(), getServer()).tick();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeData(NbtCompound nbt, CallbackInfo ci) {
        Nations.DATABASE_HANDLER.getOrCreatePlayerAbilityList(this.getUuid(), this.getServer()).updateAllAbilities();
    }

    private PlayerEntity asPlayer() {
        return (PlayerEntity) (Object) this;
    }
}

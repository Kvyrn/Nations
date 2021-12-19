package io.github.treesoid.nations.mixin;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.server.NationsServer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        NationsServer.DATABASE_HANDLER.getOrCreatePlayerAbilityList(this.getUuid(), getServer()).tick();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeData(NbtCompound nbt, CallbackInfo ci) {
        try {
            NationsServer.DATABASE_HANDLER.getOrCreatePlayerAbilityList(this.getUuid(), this.getServer()).updateAllAbilities();
            NationsServer.DATABASE_HANDLER.updateCachedPlayerData(getUuid());
        } catch (Exception e) {
            Nations.LOGGER.warn("[Nations] Failed to save player data!", e);
        }
    }

    private PlayerEntity asPlayer() {
        return (PlayerEntity) (Object) this;
    }
}

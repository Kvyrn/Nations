package io.github.treesoid.nations.mixin;

import io.github.treesoid.nations.components.NationsCKeys;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        NationsCKeys.ABILITIES.get(asPlayer()).get().tick();
    }

    private PlayerEntity asPlayer() {
        return (PlayerEntity) (Object) this;
    }
}

package io.github.treesoid.nations.mixin;

import io.github.treesoid.nations.Nations;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        Nations.DATABASE_HANDLER.removeFromCache(player.getUuid());
        Nations.DATABASE_HANDLER.getOrCreatePlayerData(player.getUuid(), player.server, true, true);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void onPlayerLeave(ServerPlayerEntity player, CallbackInfo ci) {
        Nations.DATABASE_HANDLER.removeFromCache(player.getUuid());
    }
}

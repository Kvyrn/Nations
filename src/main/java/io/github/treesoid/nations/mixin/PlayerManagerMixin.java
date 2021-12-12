package io.github.treesoid.nations.mixin;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.server.NationsServer;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        NationsServer.DATABASE_HANDLER.removeFromCache(player.getUuid());
        NationsServer.DATABASE_HANDLER.getOrCreatePlayerData(player.getUuid(), player.server, true, true);
        NationsServer.DATABASE_HANDLER.getOrCreatePlayerAbilityList(player.getUuid(), this.server).sync();
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void onPlayerLeave(ServerPlayerEntity player, CallbackInfo ci) {
        NationsServer.DATABASE_HANDLER.removeFromCache(player.getUuid());
    }
}

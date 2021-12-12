package io.github.treesoid.nations.network.s2c;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.client.NationsClient;
import io.github.treesoid.nations.client.syncobjects.ClientPlayerAbilityList;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncAbilityListPacket {
    public static final Identifier IDENTIFIER = new Identifier(Nations.modid, "sync_ability_list");

    public static void send(ServerPlayerEntity user, NbtCompound compound) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(compound);
        ServerPlayNetworking.send(user, IDENTIFIER, buf);
    }

    public static void registerReciver() {
        ClientPlayNetworking.registerGlobalReceiver(IDENTIFIER, (client, handler, buf, responseSender) -> {
            NbtCompound compound = buf.readNbt();
            client.execute(() -> {
                if (compound == null) return;
                NationsClient.abilityList = ClientPlayerAbilityList.deserialize(compound, client.player);
            });
        });
    }
}

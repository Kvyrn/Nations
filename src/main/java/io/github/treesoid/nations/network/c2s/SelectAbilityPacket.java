package io.github.treesoid.nations.network.c2s;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.util.Ability;
import io.github.treesoid.nations.abilities.util.PlayerAbilityList;
import io.github.treesoid.nations.server.NationsServer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SelectAbilityPacket {
    public static Identifier IDENTIFIER = new Identifier(Nations.modid, "select_ability");

    public static void registerReciver() {
        ServerPlayNetworking.registerGlobalReceiver(IDENTIFIER, (server, player, handler, buf, responseSender) -> {
            Identifier abilityIdentifier = buf.readIdentifier();
            server.execute(() -> {
                PlayerAbilityList abilityList = NationsServer.DATABASE_HANDLER.getOrCreatePlayerAbilityList(player.getUuid(), server);
                abilityList.selectAbility(abilityIdentifier);
            });
        });
    }

    public static void send(Ability ability) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeIdentifier(ability.identifier);
        ClientPlayNetworking.send(IDENTIFIER, buffer);
    }
}

package io.github.treesoid.nations.network.c2s;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.util.PlayerAbilityList;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ActivateAbilityPacket {
    public static Identifier IDENTIFIER = new Identifier(Nations.modid, "activate_ability");

    public static void registerReciver() {
        ServerPlayNetworking.registerGlobalReceiver(IDENTIFIER, (server, player, handler, buf, responseSender) -> server.execute(() -> {
            PlayerAbilityList abilityList = Nations.DATABASE_HANDLER.getOrCreatePlayerAbilityList(player.getUuid(), server);
            if (!abilityList.hasAbilitySelected()) System.out.println("no ability selected");
            else System.out.println("selected ability: " + abilityList.selectedAbility.ability.identifier);
            abilityList.useSelectedAbility();
        }));
    }

    public static void send() {
        ClientPlayNetworking.send(IDENTIFIER, PacketByteBufs.empty());
    }
}

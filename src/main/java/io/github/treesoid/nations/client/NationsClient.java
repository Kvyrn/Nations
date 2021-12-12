package io.github.treesoid.nations.client;

import io.github.treesoid.nations.client.syncobjects.ClientPlayerAbilityList;
import io.github.treesoid.nations.network.s2c.AddVelocityPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NationsClient implements ClientModInitializer {
    public static ClientPlayerAbilityList abilityList;

    @Override
    public void onInitializeClient() {
        NationsKeybinds.register();
        registerClientNetworkRecivers();
    }

    private static void registerClientNetworkRecivers() {
        AddVelocityPacket.registerReciver();
    }
}

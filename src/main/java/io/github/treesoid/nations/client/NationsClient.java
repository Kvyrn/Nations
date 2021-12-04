package io.github.treesoid.nations.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NationsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NationsKeybinds.register();
        registerClientNetworkRecivers();
    }

    private static void registerClientNetworkRecivers() {
    }
}

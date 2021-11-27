package io.github.treesoid.nations.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NationsKeybinds {
    public static KeyBinding ACTIVATE_ABILITY;

    public static void register() {
        ACTIVATE_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nations.activate_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.nations"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(NationsKeybinds::tick);
    }

    @SuppressWarnings("UnusedAssignment")
    private static void tick(MinecraftClient client) {
        boolean wasPressed = false;
        while (ACTIVATE_ABILITY.wasPressed()) {
            wasPressed = true;
        }
        if (wasPressed) {

        }
    }
}

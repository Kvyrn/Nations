package io.github.treesoid.nations.client;

import io.github.treesoid.nations.abilities.FartJumpAbility;
import io.github.treesoid.nations.network.c2s.ActivateAbilityPacket;
import io.github.treesoid.nations.network.c2s.SelectAbilityPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NationsKeybinds {
    public static KeyBinding ACTIVATE_ABILITY;
    public static KeyBinding SELECT_ABILITY;

    public static void register() {
        ACTIVATE_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nations.activate_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.nations"
        ));

        SELECT_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nations.select_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category.nations"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(NationsKeybinds::tick);
    }

    private static void tick(MinecraftClient client) {
        handleActivateAbility();
        handleSelectAbility();
    }

    private static void handleSelectAbility() {
        boolean wasPressed = false;
        while (SELECT_ABILITY.wasPressed()) {
            wasPressed = true;
        }
        if (wasPressed) {
            SelectAbilityPacket.send(FartJumpAbility.INSTANCE);
        }
    }

    private static void handleActivateAbility() {
        boolean wasPressed = false;
        while (ACTIVATE_ABILITY.wasPressed()) {
            wasPressed = true;
        }
        if (wasPressed) {
            ActivateAbilityPacket.send();
        }
    }
}

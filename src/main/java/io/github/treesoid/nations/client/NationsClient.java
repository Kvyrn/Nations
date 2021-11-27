package io.github.treesoid.nations.client;

import io.github.treesoid.nations.abilities.player.PlayerAbility;
import io.github.treesoid.nations.abilities.player.PlayerAbilityList;
import io.github.treesoid.nations.components.NationsCKeys;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class NationsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(NationsClient::onHudRender);
        NationsKeybinds.register();
    }

    private static void onHudRender(MatrixStack matrixStack, float tickDelta) {
        matrixStack.push();
        MinecraftClient client = MinecraftClient.getInstance();
        @SuppressWarnings("ConstantConditions")
        PlayerAbilityList abilityList = NationsCKeys.ABILITIES.get(client.player).get();
        int y = 10;

        for (PlayerAbility ability : abilityList.abilities) {
            client.textRenderer.drawWithShadow(matrixStack, ability.ability.identifier.toString(), 10, y, 0x00FF00);
            y += 10;
        }
        matrixStack.pop();
    }
}

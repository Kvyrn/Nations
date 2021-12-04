package io.github.treesoid.nations.client.gui;

import io.github.treesoid.nations.Nations;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class AbilitySelectorGui extends Screen {
    private static final Identifier BACKGROUND = new Identifier(Nations.modid, "textures/gui/ability_selector/background.png");
    private static final Identifier BACKGROUND_HIGHLITED = new Identifier(Nations.modid, "textures/gui/ability_selector/background_highlited.png");

    protected AbilitySelectorGui() {
        super(new TranslatableText("gui.nations.abilitySelector.title"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (client == null || client.player == null) return;
        //PlayerAbilityList abilityList = NationsCKeys.ABILITIES.get(client.player).get();

        matrices.push();
        // TODO change to favourites, all abilities for testing
        /*for (PlayerAbility ability : abilityList.abilities) {
            double x = (width / 2d) + Math.sin(abilityList.abilityIntervalAngle);
            double y = (height / 2d) + Math.cos(abilityList.abilityIntervalAngle);
        }*/
        matrices.pop();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

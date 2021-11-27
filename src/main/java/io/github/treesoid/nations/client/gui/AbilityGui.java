package io.github.treesoid.nations.client.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;

public class AbilityGui extends LightweightGuiDescription {
    public AbilityGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(10*18, 10*18);
        root.setInsets(Insets.ROOT_PANEL);
    }
}

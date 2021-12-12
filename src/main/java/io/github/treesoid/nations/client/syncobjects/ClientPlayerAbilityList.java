package io.github.treesoid.nations.client.syncobjects;

import io.github.treesoid.nations.abilities.FartJumpAbility;
import io.github.treesoid.nations.abilities.util.PlayerAbility;
import io.github.treesoid.nations.client.config.NationsClientConfig;
import io.github.treesoid.nations.network.c2s.ActivateAbilityPacket;
import io.github.treesoid.nations.network.c2s.SelectAbilityPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.LinkedList;
import java.util.List;

public class ClientPlayerAbilityList {
    public final ClientPlayerEntity player;
    public final List<PlayerAbility> abilities;
    public List<PlayerAbility> favourites = new LinkedList<>();
    public PlayerAbility selectedAbility = null;
    public double angle = 360;

    public ClientPlayerAbilityList(ClientPlayerEntity player, List<PlayerAbility> abilities) {
        this.player = player;
        this.abilities = abilities;
    }

    public void updateFavourites() {
        favourites = abilities.stream().filter(PlayerAbility::isFavourite).toList();
        angle = favourites.size() < 1 ? 360 : 360d / favourites.size();
    }

    public void activateAbility() {
        if (selectedAbility != null && !selectedAbility.hasCooldown()) {
            ActivateAbilityPacket.send();
        }
    }

    public void selectAbility(MinecraftClient client) {
        if (NationsClientConfig.CONFIG.cycleToSelect) {
            int selected = favourites.indexOf(selectedAbility);
            if (selected < 0 || selected >= favourites.size()) {
                SelectAbilityPacket.send(favourites.get(0).ability);
            } else {
                SelectAbilityPacket.send(favourites.get(selected + 1).ability);
            }
        } else {
            SelectAbilityPacket.send(FartJumpAbility.INSTANCE);
        }
        if (client.player != null) {
            client.player.sendMessage(new LiteralText("Ability selected"), false);
        }
    }
}

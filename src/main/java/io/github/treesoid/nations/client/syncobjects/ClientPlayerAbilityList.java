package io.github.treesoid.nations.client.syncobjects;

import io.github.treesoid.nations.abilities.util.PlayerAbility;
import net.minecraft.client.network.ClientPlayerEntity;

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
}

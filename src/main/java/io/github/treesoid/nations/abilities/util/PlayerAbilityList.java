package io.github.treesoid.nations.abilities.util;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.helper.ServerPlayerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class PlayerAbilityList {
    public final UUID player;
    public final List<PlayerAbility> abilities = new LinkedList<>();
    private final MinecraftServer server;
    public PlayerAbility selectedAbility = null;
    public List<PlayerAbility> favourites = new ArrayList<>();
    // In radians
    public double abilityIntervalAngle = 0;

    public PlayerAbilityList(UUID player, MinecraftServer server) {
        this.server = server;
        this.player = player;
    }

    public boolean addAbility(Ability ability) {
        if (!ServerPlayerHelper.playerOnline(player, server)) return false;
        PlayerEntity playerEntity = ServerPlayerHelper.getPlayer(player, server);
        if (!hasAbility(ability) && ability.canObtain(playerEntity)) {
            PlayerAbility newAbility = new PlayerAbility(playerEntity, ability);
            abilities.add(newAbility);
            Nations.DATABASE_HANDLER.addPlayerAbility(newAbility, player);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAbility(Ability ability) {
        return abilities.removeIf(ability1 -> {
            if (ability.matches(ability1)) {
                Nations.DATABASE_HANDLER.removePlayerAbility(ability1.ability, player);
                return true;
            }
            return false;
        });
    }

    public void tick() {
        for (PlayerAbility ability : abilities) {
            ability.tickCooldown();
        }
    }

    public boolean hasAbility(Ability ability) {
        return abilities.stream().anyMatch(ability::matches);
    }

    public boolean hasAbilitySelected() {
        return selectedAbility != null;
    }

    public boolean favouriteAbility(Ability ability) {
        return favouriteAbility(ability, true);
    }

    public boolean favouriteAbility(Ability ability, boolean favourite) {
        AtomicBoolean output = new AtomicBoolean(false);
        this.abilities.stream()
                .filter(ability::matches)
                .filter(ability1 -> ability1.isFavourite() != favourite)
                .forEach(ability1 -> {
                    ability1.setFavourite(favourite);
                    Nations.DATABASE_HANDLER.updatePlayerAbility(ability1, player);
                    output.set(true);
                });
        this.updateFavourites();
        return output.get();
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean selectAbility(Ability ability) {
        PlayerAbility prevSelected = selectedAbility;
        if (ability == null) {
            this.selectedAbility = null;
            if (prevSelected != null) {
                Nations.DATABASE_HANDLER.updatePlayerAbility(prevSelected, player);
            }
            return true;
        }
        Optional<PlayerAbility> optionalPlayerAbility = abilities.stream().filter(ability::matches).findFirst();
        if (optionalPlayerAbility.isEmpty()) return false;
        this.selectedAbility = optionalPlayerAbility.get();
        Nations.DATABASE_HANDLER.updatePlayerAbility(prevSelected, player);
        Nations.DATABASE_HANDLER.updatePlayerAbility(selectedAbility, player);
        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean selectAbility(Identifier ability) {
        return selectAbility(Nations.ABILITY_REGISTRY.get(ability));
    }

    public void useSelectedAbility() {
        if (hasAbilitySelected()) {
            selectedAbility.use();
        }
    }

    public void updateFavourites() {
        favourites = abilities.stream()
                .filter(PlayerAbility::isFavourite)
                .collect(Collectors.toList());
        abilityIntervalAngle = Math.toRadians(favourites.size() == 0 ? 360 : 360d / favourites.size());
    }

    public boolean isOnline() {
        return ServerPlayerHelper.playerOnline(player, server);
    }

    public void updateAllAbilities() {
        for (PlayerAbility ability : abilities) {
            Nations.DATABASE_HANDLER.updatePlayerAbility(ability, player);
        }
    }

    public NbtCompound serialize() {
        NbtCompound compound = new NbtCompound();
        NbtList list = new NbtList();
        for (PlayerAbility ability : abilities) {
            list.add(ability.serialize());
        }
        compound.put("abilities", list);
        if (hasAbilitySelected()) {
            compound.putString("selctedAbility", selectedAbility.ability.identifier.toString());
        }
        return compound;
    }

    public void sync() {
    }
}

package io.github.treesoid.nations.abilities.player;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.storage.Ability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerAbilityList {
    public final PlayerEntity player;
    public final List<PlayerAbility> abilities = new LinkedList<>();
    public PlayerAbility selectedAbility = null;

    public PlayerAbilityList(PlayerEntity player) {
        this.player = player;
    }

    public PlayerAbilityList(UUID player, MinecraftServer server) {
        this(server.getPlayerManager().getPlayer(player));
    }

    public boolean addAbility(Ability ability) {
        if (!hasAbility(ability) && ability.canObtain(player)) {
            abilities.add(new PlayerAbility(player, ability));
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAbility(Ability ability) {
        return abilities.removeIf(ability::matches);
    }

    public void tick() {
        for (PlayerAbility ability : abilities) {
            ability.tickCooldown();
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

    public boolean hasAbility(Ability ability) {
        return abilities.stream().anyMatch(ability::matches);
    }

    public boolean hasAbilitySelected() {
        return selectedAbility != null;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean selectAbility(Ability ability) {
        if (ability == null) {
            this.selectedAbility = null;
            return true;
        }
        Optional<PlayerAbility> optionalPlayerAbility = abilities.stream().filter(ability::matches).findFirst();
        if (optionalPlayerAbility.isEmpty()) return false;
        this.selectedAbility = optionalPlayerAbility.get();
        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean selectAbility(Identifier ability) {
        if (ability == null) {
            this.selectedAbility = null;
            return true;
        }
        Optional<PlayerAbility> optionalPlayerAbility = abilities.stream().filter(ability1 -> ability1.ability.identifier.equals(ability)).findFirst();
        if (optionalPlayerAbility.isEmpty()) return false;
        this.selectedAbility = optionalPlayerAbility.get();
        return true;
    }

    public void useSelectedAbility() {
        if (hasAbilitySelected()) {
            selectedAbility.use();
        }
    }

    public static PlayerAbilityList deserialize(NbtCompound compound, PlayerEntity player) {
        PlayerAbilityList object = new PlayerAbilityList(player);
        try {
            NbtList list = compound.getList("abilities", 10);
            for (NbtElement element : list) {
                object.abilities.add(PlayerAbility.deserialize((NbtCompound) element, player));
            }
            if (compound.contains("selectedAbility")) {
                Identifier selectedAbilityIdentifier = new Identifier(compound.getString("selectedAbility"));
                if (Nations.ABILITY_REGISTRY.containsKey(selectedAbilityIdentifier)) {
                    object.selectAbility(selectedAbilityIdentifier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return object;
        }
        return object;
    }
}

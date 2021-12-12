package io.github.treesoid.nations.client.syncobjects;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.FartJumpAbility;
import io.github.treesoid.nations.abilities.util.PlayerAbility;
import io.github.treesoid.nations.client.config.NationsClientConfig;
import io.github.treesoid.nations.network.c2s.ActivateAbilityPacket;
import io.github.treesoid.nations.network.c2s.RequestSyncAbilityListPacket;
import io.github.treesoid.nations.network.c2s.SelectAbilityPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class ClientPlayerAbilityList {
    public final ClientPlayerEntity player;
    public final List<PlayerAbility> abilities;
    public List<PlayerAbility> favourites = new LinkedList<>();
    public PlayerAbility selectedAbility = null;
    public double angle = 360;

    private ClientPlayerAbilityList(ClientPlayerEntity player, List<PlayerAbility> abilities) {
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

    public static ClientPlayerAbilityList deserialize(NbtCompound compound, ClientPlayerEntity entity) {
        NbtList list = compound.getList("abilities", 10);
        List<PlayerAbility> playerAbilities = new LinkedList<>();
        Identifier selectedAbilityid = new Identifier(compound.getString("selectedAbility"));
        PlayerAbility selectedAbility = null;
        for (NbtElement element : list) {
            NbtCompound abilityCompound = (NbtCompound) element;
            Identifier abilityId = new Identifier(abilityCompound.getString("ability"));
            PlayerAbility ability = new PlayerAbility(entity, Nations.ABILITY_REGISTRY.get(abilityId));
            ability.setFavourite(abilityCompound.getBoolean("favourite"));
            ability.setCooldown(abilityCompound.getInt("cooldown"));
            if (abilityId.equals(selectedAbilityid)) selectedAbility = ability;
            playerAbilities.add(ability);
        }
        ClientPlayerAbilityList object = new ClientPlayerAbilityList(entity, playerAbilities);
        object.updateFavourites();
        if (selectedAbility != null) {
            object.selectedAbility = selectedAbility;
        }
        return object;
    }

    public void requestSync() {
        RequestSyncAbilityListPacket.send();
    }
}

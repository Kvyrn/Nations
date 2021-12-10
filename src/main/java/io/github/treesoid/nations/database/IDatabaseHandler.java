package io.github.treesoid.nations.database;

import io.github.treesoid.nations.abilities.util.Ability;
import io.github.treesoid.nations.abilities.util.PlayerAbility;
import io.github.treesoid.nations.abilities.util.PlayerAbilityList;
import io.github.treesoid.nations.storage.PlayerData;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnusedReturnValue")
public interface IDatabaseHandler {
    PlayerAbilityList getOrCreatePlayerAbilityList(UUID uuid, MinecraftServer server, boolean forceRefresh, boolean cache);

    default PlayerAbilityList getOrCreatePlayerAbilityList(UUID uuid, MinecraftServer server, boolean forceRefresh) {
        return getOrCreatePlayerAbilityList(uuid, server, forceRefresh, true);
    }

    default PlayerAbilityList getOrCreatePlayerAbilityList(boolean cache, UUID uuid, MinecraftServer server) {
        return getOrCreatePlayerAbilityList(uuid, server, false, cache);
    }

    default PlayerAbilityList getOrCreatePlayerAbilityList(UUID uuid, MinecraftServer server) {
        return getOrCreatePlayerAbilityList(uuid, server, false, true);
    }

    default int removePlayerData(PlayerData data) {
        return removePlayerData(data.uuid);
    }

    int removePlayerData(UUID uuid);

    int updatePlayerData(PlayerData data);

    default PlayerData getOrCreatePlayerData(UUID uuid, MinecraftServer server) {
        return getOrCreatePlayerData(uuid, server, false, true);
    }

    PlayerData getOrCreatePlayerData(UUID uuid, MinecraftServer server, boolean forceRefresh, boolean cache);

    List<UUID> listHoldersOfAbility(Ability ability);

    int updatePlayerAbility(PlayerAbility ability, UUID uuid);

    int addPlayerAbility(PlayerAbility ability, UUID player);

    int removePlayerAbility(Ability ability, UUID player);

    Ability getSelectedAbility(UUID player);

    void setSelectedAbility(UUID player, Ability ability);

    void removeAbilityListFromCache(UUID uuid);

    void removePlayerDataFromCache(UUID uuid);

    default void removeFromCache(UUID uuid) {
        removeAbilityListFromCache(uuid);
        removePlayerDataFromCache(uuid);
    }
}

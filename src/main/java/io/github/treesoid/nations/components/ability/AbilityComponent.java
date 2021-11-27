package io.github.treesoid.nations.components.ability;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.github.treesoid.nations.abilities.player.PlayerAbilityList;
import io.github.treesoid.nations.components.NationsCKeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class AbilityComponent implements IAbilityComponent, AutoSyncedComponent {
    private PlayerAbilityList data = null;
    private final PlayerEntity provider;

    public AbilityComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    @Override
    public PlayerAbilityList get() {
        if (data == null) {
            data = new PlayerAbilityList(provider);
        }
        return data;
    }

    @Override
    public void set(PlayerAbilityList abilities) {
        data = abilities;
        NationsCKeys.ABILITIES.sync(provider);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        data = PlayerAbilityList.deserialize(tag.getCompound("abilities"), provider);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (data == null) return;
        tag.put("abilities", data.serialize());
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player.getUuid().equals(provider.getUuid());
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        AutoSyncedComponent.super.writeSyncPacket(buf, recipient);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        AutoSyncedComponent.super.applySyncPacket(buf);
    }
}

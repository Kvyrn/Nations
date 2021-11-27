package io.github.treesoid.nations.components;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import io.github.treesoid.nations.components.ability.AbilityComponent;
import net.minecraft.entity.player.PlayerEntity;

public class NationsComponentRegistry implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, NationsCKeys.ABILITIES, AbilityComponent::new);
    }
}

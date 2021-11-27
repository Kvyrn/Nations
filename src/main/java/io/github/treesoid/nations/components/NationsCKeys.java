package io.github.treesoid.nations.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.treesoid.nations.components.ability.IAbilityComponent;
import net.minecraft.util.Identifier;

import static io.github.treesoid.nations.Nations.modid;

public class NationsCKeys {
    public static final Identifier ABILITIES_IDENTIFIER = new Identifier(modid, "abilities");
    public static final ComponentKey<IAbilityComponent> ABILITIES = ComponentRegistry.getOrCreate(ABILITIES_IDENTIFIER, IAbilityComponent.class);
}

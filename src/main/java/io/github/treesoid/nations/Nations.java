package io.github.treesoid.nations;

import io.github.treesoid.nations.abilities.FartJumpAbility;
import io.github.treesoid.nations.abilities.storage.Ability;
import io.github.treesoid.nations.commands.AbilityCommand;
import io.github.treesoid.nations.commands.argument.AbilityArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class Nations implements ModInitializer {
    public static final String modid = "nations";
    public static final HashMap<Identifier, Ability> ABILITY_REGISTRY = new HashMap<>();

    @Override
    public void onInitialize() {
        //noinspection CodeBlock2Expr
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            AbilityCommand.register(dispatcher);
        });

        ABILITY_REGISTRY.put(FartJumpAbility.IDENTIFIER, new FartJumpAbility());

        ArgumentTypes.register(modid + ":ability", AbilityArgumentType.class, new ConstantArgumentSerializer<>(AbilityArgumentType::ability));
    }
}

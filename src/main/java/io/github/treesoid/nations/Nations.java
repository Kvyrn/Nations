package io.github.treesoid.nations;

import io.github.treesoid.nations.abilities.DashAbility;
import io.github.treesoid.nations.abilities.FartJumpAbility;
import io.github.treesoid.nations.abilities.util.Ability;
import io.github.treesoid.nations.commands.AbilityCommand;
import io.github.treesoid.nations.commands.NationsCommand;
import io.github.treesoid.nations.commands.argument.AbilityArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Nations implements ModInitializer {
    public static final String modid = "nations";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final HashMap<Identifier, Ability> ABILITY_REGISTRY = new HashMap<>();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            AbilityCommand.register(dispatcher);
            NationsCommand.register(dispatcher);
        });

        ABILITY_REGISTRY.put(FartJumpAbility.IDENTIFIER, FartJumpAbility.INSTANCE);
        ABILITY_REGISTRY.put(DashAbility.IDENTIFIER, DashAbility.INSTANCE);

        NationsSounds.register();

        ArgumentTypes.register(modid + ":ability", AbilityArgumentType.class, new ConstantArgumentSerializer<>(AbilityArgumentType::ability));
    }
}

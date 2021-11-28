package io.github.treesoid.nations;

import io.github.treesoid.nations.abilities.FartJumpAbility;
import io.github.treesoid.nations.abilities.storage.Ability;
import io.github.treesoid.nations.commands.AbilityCommand;
import io.github.treesoid.nations.commands.argument.AbilityArgumentType;
import io.github.treesoid.nations.network.c2s.ActivateAbilityPacket;
import io.github.treesoid.nations.network.c2s.SelectAbilityPacket;
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

        ABILITY_REGISTRY.put(FartJumpAbility.IDENTIFIER, FartJumpAbility.INSTANCE);

        ArgumentTypes.register(modid + ":ability", AbilityArgumentType.class, new ConstantArgumentSerializer<>(AbilityArgumentType::ability));

        registerNetworkRecivers();
    }

    public void registerNetworkRecivers() {
        ActivateAbilityPacket.registerReciver();
        SelectAbilityPacket.registerReciver();
    }
}

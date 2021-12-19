package io.github.treesoid.nations.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.treesoid.nations.abilities.util.Ability;
import io.github.treesoid.nations.abilities.util.PlayerAbilityList;
import io.github.treesoid.nations.commands.argument.AbilityArgumentType;
import io.github.treesoid.nations.server.NationsServer;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AbilityCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("ability").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("target", EntityArgumentType.players())
                                .then(argument("ability", AbilityArgumentType.ability())
                                        .then(literal("grant").executes(AbilityCommand::grantAbility))
                                        .then(literal("revoke").executes(AbilityCommand::revokeAbility))))
                        .then(argument("target", EntityArgumentType.player())
                                .then(literal("list").executes(AbilityCommand::listAbilities)))
        );
    }

    private static int listAbilities(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "target");
        PlayerAbilityList abilities = NationsServer.DATABASE_HANDLER.getOrCreatePlayerAbilityList(player.getUuid(), context.getSource().getServer());

        StringBuilder builder = new StringBuilder();
        Iterator<Ability> iterator = abilities.abilities.stream().map(ability -> ability.ability).iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next().identifier.toString());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        context.getSource().sendFeedback(new TranslatableText("nations.commands.ability.list", player.getDisplayName(), builder.toString()), false);
        return 1;
    }

    private static int grantAbility(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Ability ability = AbilityArgumentType.getAbility(context, "ability");
        Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "target");
        LinkedList<Text> fails = new LinkedList<>();

        for (ServerPlayerEntity target : targets) {
            PlayerAbilityList abilityList = NationsServer.DATABASE_HANDLER.getOrCreatePlayerAbilityList(target.getUuid(), context.getSource().getServer());
            boolean succesfull = abilityList.addAbility(ability);
            if (!succesfull) {
                fails.add(target.getDisplayName());
            }
            abilityList.sync();
        }

        genOutput(context.getSource(), "grant", targets, fails);
        return targets.size() - fails.size();
    }

    private static int revokeAbility(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Ability ability = AbilityArgumentType.getAbility(context, "ability");
        Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "target");
        LinkedList<Text> fails = new LinkedList<>();

        for (ServerPlayerEntity target : targets) {
            PlayerAbilityList abilityList = NationsServer.DATABASE_HANDLER.getOrCreatePlayerAbilityList(target.getUuid(), context.getSource().getServer());
            boolean succesfull = abilityList.removeAbility(ability);
            if (!succesfull) {
                fails.add(target.getDisplayName());
            }
            abilityList.sync();
        }

        genOutput(context.getSource(), "revoke", targets, fails);
        return targets.size() - fails.size();
    }

    private static void genOutput(ServerCommandSource source, String type, Collection<ServerPlayerEntity> targets, LinkedList<Text> fails) {
        TranslatableText output;
        if (targets.size() == 1) {
            output = new TranslatableText("nations.commands.ability." + type + "." + (fails.size() > 0 ? "fail" : "success") + ".single", targets.stream().findFirst().get().getDisplayName());
        } else if (fails.size() >= targets.size()) {
            output = new TranslatableText("nations.commands.ability." + type + ".fail.multi", fails.size());
        } else if (fails.size() > 1) {
            StringBuilder failString = new StringBuilder();
            Iterator<Text> iterator = fails.iterator();
            while (iterator.hasNext()) {
                failString.append(iterator.next().getString());
                if (iterator.hasNext()) {
                    failString.append(", ");
                }
            }
            output = new TranslatableText("nations.commands.ability." + type + ".partial_success", (targets.size() - fails.size()), failString);
        } else {
            output = new TranslatableText("nations.commands.ability." + type + ".success.multi", targets.size());
        }

        if (output.getKey().contains("fail")) {
            source.sendError(output);
        } else {
            source.sendFeedback(output, true);
        }
    }
}

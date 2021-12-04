package io.github.treesoid.nations.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.util.Ability;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class AbilityArgumentType implements ArgumentType<Ability> {
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    private static final Collection<String> EXAMPLES = Arrays.asList("nations:fart_jump");
    private static final DynamicCommandExceptionType INVALID_ABILITY = new DynamicCommandExceptionType(o -> new TranslatableText("nations.commands.ability.invalid", o));

    private AbilityArgumentType() {
    }

    public static AbilityArgumentType ability() {
        return new AbilityArgumentType();
    }

    public static Ability getAbility(CommandContext<?> context, String name) {
        return context.getArgument(name, Ability.class);
    }

    @Override
    public Ability parse(StringReader reader) throws CommandSyntaxException {
        Identifier identifier = Identifier.fromCommandInput(reader);
        if (!Nations.ABILITY_REGISTRY.containsKey(identifier)) {
            throw INVALID_ABILITY.create(identifier);
        }
        return Nations.ABILITY_REGISTRY.get(identifier);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemainingLowerCase();
        Nations.ABILITY_REGISTRY.keySet().stream()
                .map(Identifier::toString)
                .filter(s -> s.contains(remaining))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

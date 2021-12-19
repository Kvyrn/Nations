package io.github.treesoid.nations;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NationsSounds {
    private static final Identifier FART_ID = new Identifier(Nations.modid, "fart");
    private static final Identifier SHORT_FART_ID = new Identifier(Nations.modid, "short_fart");

    public static final SoundEvent FART = new SoundEvent(FART_ID);
    public static final SoundEvent SHORT_FART = new SoundEvent(SHORT_FART_ID);

    public static void register() {
        Registry.register(Registry.SOUND_EVENT, FART_ID, FART);
        Registry.register(Registry.SOUND_EVENT, SHORT_FART_ID, SHORT_FART);
    }
}

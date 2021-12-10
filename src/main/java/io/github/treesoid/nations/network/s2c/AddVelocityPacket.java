package io.github.treesoid.nations.network.s2c;

import io.github.treesoid.nations.Nations;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class AddVelocityPacket {
    public static final Identifier IDENTIFIER = new Identifier(Nations.modid, "add_velocity");

    public static void send(ServerPlayerEntity user, @NotNull Vec3d velocity) {
        send(user, velocity.x, velocity.y, velocity.z);
    }

    public static void send(ServerPlayerEntity user, double x, double y, double z) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        ServerPlayNetworking.send(user, IDENTIFIER, buf);
    }

    public static void registerReciver() {
        ClientPlayNetworking.registerGlobalReceiver(IDENTIFIER, (client, handler, buf, responseSender) -> {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            client.execute(() -> {
                if (client.player != null) {
                    client.player.addVelocity(x, y, z);
                }
            });
        });
    }
}

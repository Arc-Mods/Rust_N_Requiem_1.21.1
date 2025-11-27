package com.katto.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

public record CinderBeamS2CPacket(BlockPos pos) {

    public CinderBeamS2CPacket(PacketByteBuf buf) {
        this(new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    // client-side handler: runs on client main thread
    public void handle(MinecraftClient client) {
        if (client.world == null) return;

        // Simple particle example — replace with Nitrogen RenderUtils call if you want
        client.world.addParticle(
                ParticleTypes.FLAME,
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                0.0, 0.1, 0.0
        );
    }
}
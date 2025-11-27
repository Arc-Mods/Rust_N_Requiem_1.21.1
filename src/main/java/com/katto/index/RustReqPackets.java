package com.katto.index;

import com.katto.network.CinderBeamS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class RustReqPackets {

    public static final Identifier CINDER_BEAM_ID = Identifier.of("rustreq", "cinder_beam");

    public static void registerClient() {
        // Register a packet receiver
        ClientPlayNetworking.registerGlobalReceiver(
                CINDER_BEAM_ID,
                (handler, buf, responseSender) -> {
                    // Create packet from buffer
                    CinderBeamS2CPacket packet = new CinderBeamS2CPacket(buf);

                    // Schedule on main client thread
                    MinecraftClient.getInstance().execute(() -> {
                        packet.handle(MinecraftClient.getInstance());
                    });
                }
        );
    }

    public static void registerServer() {
        // nothing for server
    }
}
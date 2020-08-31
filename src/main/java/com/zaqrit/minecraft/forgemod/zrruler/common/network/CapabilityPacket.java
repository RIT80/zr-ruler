package com.zaqrit.minecraft.forgemod.zrruler.common.network;

import java.util.Objects;
import java.util.function.Supplier;
import com.zaqrit.minecraft.forgemod.zrruler.common.capability.ZrRulerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CapabilityPacket {
    private final byte zrRulerable;

    /**
     *
     */
    public CapabilityPacket(byte zrRulerable) {
        this.zrRulerable = zrRulerable;
    }

    public static void encode(CapabilityPacket packet, PacketBuffer buffer) {
        buffer.writeByte(packet.zrRulerable);
    }

    public static CapabilityPacket decode(PacketBuffer buffer) {
        return new CapabilityPacket(buffer.readByte());
    }

    public static void handle(CapabilityPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().player;
            if (Objects.isNull(player)) {
                return;
            }
            player.getCapability(ZrRulerCapability.ZRRULERABLE_CAPABILITY)
                    .orElseThrow(() -> new IllegalArgumentException(""))
                    .setZrRulerable(packet.zrRulerable);;

        });
        context.setPacketHandled(true);
    }



}

package net.rotgruengelb.adventure_core.network.packet.c2s.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.UnknownCustomPayload;
import net.minecraft.util.Identifier;
import net.rotgruengelb.adventure_core.accessor.AdventureCoreServerCustomPlayPacketListener;

public record CustomPlayPayloadC2SPacket(CustomPayload payload) implements Packet<ServerPlayPacketListener> {
    private static final int MAX_PAYLOAD_SIZE = 32767;

    public CustomPlayPayloadC2SPacket(PacketByteBuf buf) {
        this(readPayload(buf.readIdentifier(), buf));
    }

    public CustomPlayPayloadC2SPacket(CustomPayload payload) {
        this.payload = payload;
    }

    public CustomPlayPayloadC2SPacket(Object o) {
        this((CustomPayload) o);
    }

    private static CustomPayload readPayload(Identifier id, PacketByteBuf buf) {
        PacketByteBuf.PacketReader<? extends CustomPayload> packetReader = BrandCustomPayload::new;
        return packetReader.apply(buf);
    }

    private static UnknownCustomPayload readUnknownPayload(Identifier id, PacketByteBuf buf) {
        int i = buf.readableBytes();
        if (i >= 0 && i <= MAX_PAYLOAD_SIZE) {
            buf.skipBytes(i);
            return new UnknownCustomPayload(id);
        } else {
            throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
        }
    }

    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.payload.id());
        this.payload.write(buf);
    }

    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        ((AdventureCoreServerCustomPlayPacketListener) serverPlayPacketListener).onCustomPayload(this);
    }

    public CustomPayload payload() {
        return this.payload;
    }
}

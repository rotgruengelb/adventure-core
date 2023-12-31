package net.rotgruengelb.adventure_core.accessor;

import net.rotgruengelb.adventure_core.network.packet.c2s.common.CustomPlayPayloadC2SPacket;

public interface AdventureCoreServerCustomPlayPacketListener {
    void onCustomPayload(CustomPlayPayloadC2SPacket var1);
}

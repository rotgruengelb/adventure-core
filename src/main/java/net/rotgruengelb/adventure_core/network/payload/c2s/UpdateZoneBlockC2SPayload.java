package net.rotgruengelb.adventure_core.network.payload.c2s;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.rotgruengelb.adventure_core.AdventureCore;
import net.rotgruengelb.adventure_core.block.enums.ZoneBlockMode;
import net.rotgruengelb.adventure_core.network.payload.ExpectsForceMainThread;

public class UpdateZoneBlockC2SPayload implements CustomPayload, ExpectsForceMainThread {
    public static final Identifier IDENTIFIER = new Identifier(AdventureCore.MOD_ID, "update_zone_block");
    private final BlockPos pos;
    private final ZoneBlockMode mode;
    private final boolean showZones;

    public UpdateZoneBlockC2SPayload(BlockPos pos, ZoneBlockMode mode, boolean showZones) {
        this.pos = pos;
        this.mode = mode;
        this.showZones = showZones;
    }

    public UpdateZoneBlockC2SPayload(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.mode = buf.readEnumConstant(ZoneBlockMode.class);
        byte r = buf.readByte();
        this.showZones = (r & 1) != 0;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public ZoneBlockMode getMode() {
        return this.mode;
    }

    public boolean shouldShowZones() {
        return this.showZones;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeEnumConstant(this.mode);
        int i = 0;
        if (this.showZones) {
            i |= 4;
        }
        buf.writeByte(i);
    }

    @Override
    public Identifier id() {
        return IDENTIFIER;
    }
}


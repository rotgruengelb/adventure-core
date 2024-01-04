package net.rotgruengelb.adventure_core.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.rotgruengelb.adventure_core.AdventureCore;
import net.rotgruengelb.adventure_core.block.enums.ZoneBlockMode;

public class UpdateZoneBlockC2SPacket {

    public static final Identifier UPDATE_ZONE_BLOCK_PACKET_ID = new Identifier(AdventureCore.MOD_ID, "update_zone_block");

    private final BlockPos pos;
    private final ZoneBlockMode mode;
    private final boolean showZones;

    public UpdateZoneBlockC2SPacket(BlockPos pos, ZoneBlockMode mode, boolean showZones) {
        this.pos = pos;
        this.mode = mode;
        this.showZones = showZones;
    }

    public UpdateZoneBlockC2SPacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.mode = ZoneBlockMode.valueOf(buf.readString());
        this.showZones = buf.readBoolean();
    }

    public BlockPos getPos() {
        return pos;
    }

    public ZoneBlockMode getMode() {
        return mode;
    }

    public boolean shouldShowZones() {
        return showZones;
    }

    public PacketByteBuf create() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.pos);
        buf.writeString(this.mode.toString());
        buf.writeBoolean(this.showZones);
        return buf;
    }
}

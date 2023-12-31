package net.rotgruengelb.adventure_core.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.rotgruengelb.adventure_core.AdventureCore;
import net.rotgruengelb.adventure_core.accessor.AdventureCoreServerCustomPlayPacketListener;
import net.rotgruengelb.adventure_core.block.entity.ZoneBlockBlockEntity;
import net.rotgruengelb.adventure_core.network.packet.c2s.common.CustomPlayPayloadC2SPacket;
import net.rotgruengelb.adventure_core.network.payload.ExpectsForceMainThread;
import net.rotgruengelb.adventure_core.network.payload.c2s.UpdateZoneBlockC2SPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements AdventureCoreServerCustomPlayPacketListener {

    @Shadow
    public ServerPlayerEntity player;

    @Override
    public void onCustomPayload(CustomPlayPayloadC2SPacket packet) {
        CustomPayload payload = packet.payload();
        if (payload instanceof ExpectsForceMainThread) {
            NetworkThreadUtils.forceMainThread(packet, ((ServerPlayNetworkHandler) (Object) this), this.player.getServerWorld());
        }
        if (payload instanceof UpdateZoneBlockC2SPayload zoneBlockC2SPayload) {
            if (!player.isCreativeLevelTwoOp()) {
                return;
            }
            BlockPos blockPos = zoneBlockC2SPayload.getPos();
            BlockState blockState = player.getWorld().getBlockState(blockPos);
            BlockEntity blockEntity = player.getWorld().getBlockEntity(blockPos);
            if (blockEntity instanceof ZoneBlockBlockEntity zoneBlockBlockEntity) {
                zoneBlockBlockEntity.setMode(zoneBlockC2SPayload.getMode());
                zoneBlockBlockEntity.setShowZones(zoneBlockC2SPayload.shouldShowZones());
                zoneBlockBlockEntity.markDirty();
                player.getWorld().updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        }
    }
}

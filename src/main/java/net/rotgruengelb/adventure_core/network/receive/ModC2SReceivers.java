package net.rotgruengelb.adventure_core.network.receive;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rotgruengelb.adventure_core.AdventureCore;
import net.rotgruengelb.adventure_core.block.entity.ZoneBlockBlockEntity;
import net.rotgruengelb.adventure_core.network.UpdateZoneBlockC2SPacket;

import static net.rotgruengelb.adventure_core.network.UpdateZoneBlockC2SPacket.UPDATE_ZONE_BLOCK_PACKET_ID;

public class ModC2SReceivers {
	public static void registerModC2SReceivers() {

		// UPDATE_ZONE_BLOCK_PACKET RECEIVER
		ServerPlayNetworking.registerGlobalReceiver(UPDATE_ZONE_BLOCK_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			if (player.isCreativeLevelTwoOp()) {
				UpdateZoneBlockC2SPacket packet = new UpdateZoneBlockC2SPacket(buf);
				server.execute(() -> {
					BlockPos blockPos = packet.getPos();
					BlockState blockState = player.getWorld().getBlockState(blockPos);
					BlockEntity blockEntity = player.getWorld().getBlockEntity(blockPos);
					AdventureCore.LOGGER.info("Executing packet for ZoneBlockBlockEntity at " + blockPos + " with mode " + packet.getMode()
							.toString() + " and showZones " + packet.shouldShowZones() + " on server thread");
					if (blockEntity instanceof ZoneBlockBlockEntity zoneBlockBlockEntity) {
						World world = player.getWorld();
						zoneBlockBlockEntity.setMode(packet.getMode());
						zoneBlockBlockEntity.setShowZones(packet.shouldShowZones());
						zoneBlockBlockEntity.markDirty();
						world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
					}
				});
			}
		});

		// NEXT HERE
	}
}

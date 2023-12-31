package net.rotgruengelb.adventure_core.block.entity;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.rotgruengelb.adventure_core.AdventureCore;
import net.rotgruengelb.adventure_core.accessor.AdventureCoreClientPlayerEntity;
import net.rotgruengelb.adventure_core.block.ModBlocks;
import net.rotgruengelb.adventure_core.block.custom.ZoneBlock;
import net.rotgruengelb.adventure_core.block.enums.ZoneBlockMode;
import net.rotgruengelb.adventure_core.util.math.BlockZone;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ZoneBlockBlockEntity extends BlockEntity implements BlockEntityProvider {

    private List<BlockZone> zones = new ArrayList<>();
    private ZoneBlockMode mode;
    private boolean showZones;
    private boolean powered;

    public ZoneBlockBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ZONE_BLOCK_BLOCK_ENTITY, pos, state);
        this.mode = state.get(ZoneBlock.MODE);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("showZones", this.showZones);
        nbt.put("zones", this.zonesToNbt());
        nbt.putString("mode", this.mode.toString());
    }

    private NbtElement zonesToNbt() {
        NbtCompound nbt = new NbtCompound();
        if (this.zones != null) {
            for (int i = 0; i < this.zones.size(); i++) {
                nbt.put(String.valueOf(i), this.zones.get(i).toNbt());
            }
        }
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.showZones = nbt.getBoolean("showZones");
        this.zones = this.nbtToZones(nbt.getCompound("zones"));
        try {
            this.mode = ZoneBlockMode.valueOf(nbt.getString("mode"));
        } catch (IllegalArgumentException e) {
            this.mode = ZoneBlockMode.TRIGGER;
        }
        this.updateBlockMode();
    }

    private void updateBlockMode() {
        if (this.world != null) {
            BlockPos blockPos = this.getPos();
            BlockState blockState = this.world.getBlockState(blockPos);
            if (blockState.isOf(ModBlocks.ZONE_BLOCK)) {
                this.world.setBlockState(blockPos, blockState.with(ZoneBlock.MODE, this.mode), 2);
            }
        }
    }

    private List<BlockZone> nbtToZones(NbtCompound zones) {
        List<BlockZone> zonesList = new ArrayList<>();
        for (String key : zones.getKeys()) {
            zonesList.add(BlockZone.fromNbt(zones.get(key)));
        }
        return zonesList;
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ZoneBlockBlockEntity(pos, state);
    }

    public boolean openScreen(PlayerEntity player) {
        if (!player.isCreativeLevelTwoOp()) {
            return false;
        } else {
            if (player.getEntityWorld().isClient) {
                ((AdventureCoreClientPlayerEntity) player).adventure_core$openZoneBlockScreen(this);
            }
            return true;
        }
    }

    @Override
    public void markDirty() {
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        } super.markDirty();
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public ZoneBlockMode getMode() {
        return this.mode;
    }

    public void setMode(ZoneBlockMode mode) {
        this.mode = mode;
        BlockState blockState = this.world.getBlockState(this.getPos());
        if (blockState.isOf(Blocks.STRUCTURE_BLOCK)) {
            this.world.setBlockState(this.getPos(), blockState.with(ZoneBlock.MODE, mode), 2);
        }
    }

    public List<BlockZone> getZones() {
        return this.zones;
    }

    public void setZones(List<BlockZone> zones) {
        this.zones = zones;
    }

    public boolean shouldShowZones() {
        return this.showZones;
    }

    public void setShowZones(boolean b) {
        this.showZones = b;
    }
}

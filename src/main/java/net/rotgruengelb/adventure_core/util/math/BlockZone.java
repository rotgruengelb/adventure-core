package net.rotgruengelb.adventure_core.util.math;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public record BlockZone(BlockPos pos1, BlockPos pos2) {

    public static BlockZone fromNbt(NbtElement nbt) {
        NbtList nbtList = (NbtList) nbt;
        BlockPos blockPos1 = blockPosFromNbtIntArray((NbtIntArray) nbtList.get(0));
        BlockPos blockPos2 = blockPosFromNbtIntArray((NbtIntArray) nbtList.get(1));
        return new BlockZone(blockPos1, blockPos2);
    }

    public static BlockPos blockPosFromNbtIntArray(NbtIntArray nbtIntArray) {
        return new BlockPos(nbtIntArray.get(0).intValue(), nbtIntArray.get(1).intValue(), nbtIntArray.get(2).intValue());
    }

    public NbtElement toNbt() {
        NbtList nbtList = new NbtList();
        nbtList.add(new NbtIntArray(blockPosToList(this.pos1)));
        nbtList.add(new NbtIntArray(blockPosToList(this.pos2)));
        return nbtList;
    }

    public static List<Integer> blockPosToList(BlockPos pos) {
        List<Integer> posList = new ArrayList<>();
        posList.add(pos.getX());
        posList.add(pos.getY());
        posList.add(pos.getZ());
        return posList;
    }
}

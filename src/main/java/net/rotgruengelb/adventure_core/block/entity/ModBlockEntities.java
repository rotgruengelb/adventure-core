package net.rotgruengelb.adventure_core.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.rotgruengelb.adventure_core.AdventureCore;
import net.rotgruengelb.adventure_core.block.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<ZoneBlockBlockEntity> ZONE_BLOCK_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(AdventureCore.MOD_ID, "zone_block_block_entity"),
            FabricBlockEntityTypeBuilder.create(ZoneBlockBlockEntity::new, ModBlocks.ZONE_BLOCK).build());

    public static void registerModBlockEntities() {
        AdventureCore.LOGGER.info("Registering ModBlockEntities for " + AdventureCore.MOD_ID);
    }
}

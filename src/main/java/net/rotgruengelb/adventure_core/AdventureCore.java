package net.rotgruengelb.adventure_core;

import net.fabricmc.api.ModInitializer;
import net.rotgruengelb.adventure_core.block.ModBlocks;
import net.rotgruengelb.adventure_core.block.entity.ModBlockEntities;
import net.rotgruengelb.adventure_core.network.receive.ModC2SReceivers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventureCore implements ModInitializer {

	public static final String MOD_ID = "adventure_core";
	public static final Logger LOGGER = LoggerFactory.getLogger("adventure_core");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModC2SReceivers.registerModC2SReceivers();
	}
}
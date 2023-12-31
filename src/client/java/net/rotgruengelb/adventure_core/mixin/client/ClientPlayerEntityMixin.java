package net.rotgruengelb.adventure_core.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.rotgruengelb.adventure_core.AdventureCoreClient;
import net.rotgruengelb.adventure_core.accessor.AdventureCoreClientPlayerEntity;
import net.rotgruengelb.adventure_core.block.entity.ZoneBlockBlockEntity;
import net.rotgruengelb.adventure_core.client.gui.screen.ingame.ZoneBlockScreen;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.client.network.ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements AdventureCoreClientPlayerEntity {
    @Unique
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void adventure_core$openZoneBlockScreen(ZoneBlockBlockEntity zoneBlock) {
        this.client.setScreen(new ZoneBlockScreen(zoneBlock));
        Logger LOGGER = AdventureCoreClient.CLIENT_LOGGER;
        LOGGER.info(zoneBlock.getMode().asString());
        LOGGER.info(String.format("%s zones: ", zoneBlock.getPos().toString()) + zoneBlock.getZones().toString());
    }
}

package net.rotgruengelb.adventure_core.client.gui.screen.ingame;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.c2s.common.*;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.network.packet.s2c.common.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.rotgruengelb.adventure_core.AdventureCore;
import net.rotgruengelb.adventure_core.AdventureCoreClient;
import net.rotgruengelb.adventure_core.block.ModBlocks;
import net.rotgruengelb.adventure_core.block.entity.ZoneBlockBlockEntity;
import net.rotgruengelb.adventure_core.block.enums.ZoneBlockMode;
import net.rotgruengelb.adventure_core.network.packet.c2s.common.CustomPlayPayloadC2SPacket;
import net.rotgruengelb.adventure_core.network.payload.c2s.UpdateZoneBlockC2SPayload;
import org.lwjgl.glfw.GLFW;

@Environment(value = EnvType.CLIENT)
public class ZoneBlockScreen extends Screen {
    private static final ImmutableList<ZoneBlockMode> MODES = ImmutableList.copyOf(ZoneBlockMode.values());
    private static final Text SHOW_ZONES_TEXT = Text.translatable("text.adventure_core.zone_block.screen.show_zones");
    private final ZoneBlockBlockEntity zoneBlock;
    private final Text title = Text.translatable("text.adventure_core.zone_block.screen.title");
    private ZoneBlockMode mode = ZoneBlockMode.TRIGGER;
    private boolean showZones;
    private CyclingButtonWidget<Boolean> buttonShowZones;

    public ZoneBlockScreen(ZoneBlockBlockEntity zoneBlock) {
        super(Text.translatable(ModBlocks.ZONE_BLOCK.getTranslationKey()));
        this.zoneBlock = zoneBlock;
    }

    private void done() {
        if (this.updateZoneBlock()) {
            this.client.setScreen(null);
        }
    }

    private void cancel() {
        this.zoneBlock.setMode(this.mode);
        AdventureCoreClient.CLIENT_LOGGER.info(String.valueOf(this.mode));
        this.zoneBlock.setShowZones(this.showZones);
        this.client.setScreen(null);
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());
        this.mode = this.zoneBlock.getMode();
        AdventureCoreClient.CLIENT_LOGGER.info(String.valueOf(this.mode));
        AdventureCoreClient.CLIENT_LOGGER.info(this.zoneBlock.getMode().asString());
        this.showZones = this.zoneBlock.shouldShowZones();
        this.buttonShowZones = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(this.zoneBlock.shouldShowZones()).omitKeyText().build(this.width / 2 + 4 + 100, 80, 50, 20, Text.of("Show Zones"), (button, showBoundingBox) -> this.zoneBlock.setShowZones(showBoundingBox)));
        this.addDrawableChild(CyclingButtonWidget.builder((ZoneBlockMode value) -> Text.translatable("text.adventure_core.zone_block.mode." + value.asString())).values(MODES).omitKeyText().initially(this.mode).build(this.width / 2 - 4 - 150, 185, 50, 20, Text.literal("MODE"), (button, mode) -> {
            this.zoneBlock.setMode(mode);
            this.updateWidgets(mode);
        }));
        this.updateWidgets(this.mode);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.init(client, width, height);
    }

    private void updateWidgets(ZoneBlockMode mode) {
        switch (mode) {
            case TRIGGER: {
                break;
            }
//            case DENY_BREAK: {
//                break;
//            }
//            case DENY_PLACE: {
//                break;
//            }
//            case ALLOW_BREAK: {
//                break;
//            }
//            case ALLOW_PLACE: {
//                break;
//            }
        }
    }

    private boolean updateZoneBlock() {
        this.client.getNetworkHandler().sendPacket(new CustomPlayPayloadC2SPacket(new UpdateZoneBlockC2SPayload(this.zoneBlock.getPos(), this.zoneBlock.getMode(), this.zoneBlock.shouldShowZones())));
        return true;
    }

    @Override
    public void close() {
        this.cancel();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        ZoneBlockMode zoneBlockMode = this.zoneBlock.getMode();
        context.drawTextWithShadow(this.textRenderer, SHOW_ZONES_TEXT, this.width / 2 + 154 - this.textRenderer.getWidth(SHOW_ZONES_TEXT), 70, 0xA0A0A0);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, zoneBlockMode.asText(), this.width / 2 - 153, 174, 0xA0A0A0);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}




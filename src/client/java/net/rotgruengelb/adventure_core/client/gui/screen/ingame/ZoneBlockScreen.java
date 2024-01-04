package net.rotgruengelb.adventure_core.client.gui.screen.ingame;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.rotgruengelb.adventure_core.block.ModBlocks;
import net.rotgruengelb.adventure_core.block.ZoneBlock;
import net.rotgruengelb.adventure_core.block.entity.ZoneBlockBlockEntity;
import net.rotgruengelb.adventure_core.block.enums.ZoneBlockMode;
import net.rotgruengelb.adventure_core.network.UpdateZoneBlockC2SPacket;
import org.lwjgl.glfw.GLFW;

import static net.rotgruengelb.adventure_core.network.UpdateZoneBlockC2SPacket.UPDATE_ZONE_BLOCK_PACKET_ID;

@Environment(value = EnvType.CLIENT)
public class ZoneBlockScreen extends Screen {
	private static final ImmutableList<ZoneBlockMode> MODES = ImmutableList.copyOf(ZoneBlockMode.values());
	private static final Text SHOW_ZONES_TEXT = Text.translatable("text.adventure_core.zone_block.screen.show_zones");
	private static final Text title = Text.translatable("text.adventure_core.zone_block.screen.title");
	private final BlockPos pos;
	private final ZoneBlockMode originalMode;
	private final boolean originalShowZones;
	private ZoneBlockMode newMode;
	private boolean newShowZones;
	private CyclingButtonWidget<Boolean> buttonShowZones;
	private CyclingButtonWidget<ZoneBlockMode> buttonMode;

	public ZoneBlockScreen(ZoneBlockBlockEntity zoneBlock) {
		super(title);
		this.pos = zoneBlock.getPos();
		this.originalMode = zoneBlock.getMode();
		this.originalShowZones = zoneBlock.shouldShowZones();
		this.newMode = this.originalMode;
		this.newShowZones = this.originalShowZones;
	}

	private void done() {
		this.clientBlockState(this.newMode);
		ClientPlayNetworking.send(UPDATE_ZONE_BLOCK_PACKET_ID, new UpdateZoneBlockC2SPacket(this.pos, this.newMode, this.newShowZones).create());
		this.client.setScreen(null);
	}

	@Override
	protected void init() {
		this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done())
				.dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
		this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.close())
				.dimensions(this.width / 2 + 4, 210, 150, 20).build());
		this.buttonShowZones = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(this.newShowZones)
				.omitKeyText()
				.build(this.width / 2 + 4 + 100, 80, 50, 20, Text.of("Show Zones"), (button, showBoundingBox) -> {
					this.newShowZones = button.getValue();
				}));
		this.buttonMode = this.addDrawableChild(CyclingButtonWidget.builder((ZoneBlockMode value) -> Text.translatable("text.adventure_core.zone_block.mode." + value.asString()))
				.values(MODES).omitKeyText().initially(this.newMode)
				.build(this.width / 2 - 4 - 150, 185, 50, 20, Text.literal("MODE"), (button, blockMode) -> {
					this.newMode = button.getValue();
					this.clientBlockState(this.newMode);
					this.updateWidgets(this.newMode);
				}));
		this.updateWidgets(this.newMode);
		this.clientBlockState(this.newMode);
	}

	private void clientBlockState(ZoneBlockMode mode) {
		if (client != null) {
			client.execute(() -> {
				BlockState blockState = client.world.getBlockState(this.pos);
				if (blockState.isOf(ModBlocks.ZONE_BLOCK)) {
					client.world.setBlockState(this.pos, blockState.with(ZoneBlock.MODE, mode), 2);
				}
			});
		}
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

	@Override
	public void close() {
		this.client.setScreen(null);
		this.clientBlockState(this.originalMode);
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
		context.drawTextWithShadow(this.textRenderer, SHOW_ZONES_TEXT, this.width / 2 + 154 - this.textRenderer.getWidth(SHOW_ZONES_TEXT), 70, 0xA0A0A0);
		context.drawCenteredTextWithShadow(this.textRenderer, title, this.width / 2, 10, 0xFFFFFF);
		context.drawTextWithShadow(this.textRenderer, this.newMode.asText(), this.width / 2 - 153, 174, 0xA0A0A0);
	}

	@Override
	public boolean shouldPause() { return false; }
}




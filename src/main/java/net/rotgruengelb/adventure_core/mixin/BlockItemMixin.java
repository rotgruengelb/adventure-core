package net.rotgruengelb.adventure_core.mixin;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

	@Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
			at = @At("HEAD"),
			cancellable = true)
	private void place__cancelPlacementIfNotAllowed(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
		if (context.getBlockPos().equals(new BlockPos(0, 100, 0))) {
			cir.setReturnValue(net.minecraft.util.ActionResult.FAIL);
		}
	}
}

package net.rotgruengelb.adventure_core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.impl.networking.NetworkingImpl;
import net.minecraft.network.NetworkState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.listener.ServerPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import net.rotgruengelb.adventure_core.AdventureCore;
import net.rotgruengelb.adventure_core.network.packet.c2s.common.CustomPlayPayloadC2SPacket;
import net.rotgruengelb.adventure_core.network.payload.c2s.UpdateZoneBlockC2SPayload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Function;

@Mixin(targets = "net.minecraft.network.NetworkState$InternalPacketHandler")
public class NetworkStateInternalPacketHandlerMixin<T extends PacketListener> {
    @Shadow @Final
    Object2IntMap<Class<? extends Packet<? super T>>> packetIds;

    @ModifyVariable(method = "register", at = @At("HEAD"), argsOnly = true)
    private Function<PacketByteBuf, Packet<?>> replaceCustomPlayPayloadFactory(Function<PacketByteBuf, Packet<?>> original, Class<?> type) {
        if (type == CustomPlayPayloadC2SPacket.class) {
            return buf -> {
                try {
                    NetworkingImpl.FACTORY_RETAIN.set(true);
                    return new CustomPlayPayloadC2SPacket(buf);
                } finally {
                    NetworkingImpl.FACTORY_RETAIN.set(false);
                }
            };
        }
        return original;
    }

    @ModifyReturnValue(method = "register", at = @At("RETURN"))
    private NetworkState.InternalPacketHandler extend(NetworkState.InternalPacketHandler original) {
        var return_val = original;
        if (this.packetIds.containsKey(CustomPlayPayloadC2SPacket.class)) {
            return return_val;
        } else if (this.packetIds.containsKey(UpdateStructureBlockC2SPacket.class)) {
            AdventureCore.LOGGER.info("Registering CustomPlayPayloadC2SPacket for " + AdventureCore.MOD_ID);
            return_val = original.register(CustomPlayPayloadC2SPacket.class, CustomPlayPayloadC2SPacket::new);
        }
        return return_val;
    }
}

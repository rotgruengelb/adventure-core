package net.rotgruengelb.adventure_core.state;

import net.minecraft.state.property.EnumProperty;
import net.rotgruengelb.adventure_core.block.enums.ZoneBlockMode;

public class ModProperties {

    public static final EnumProperty<ZoneBlockMode> ZONE_BLOCK_MODE = EnumProperty.of("mode", ZoneBlockMode.class);

}

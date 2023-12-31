/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 */
package net.rotgruengelb.adventure_core.block.enums;

import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

public enum ZoneBlockMode implements StringIdentifiable {
    TRIGGER("trigger"),
    DENY_BREAK("deny_break"),
    DENY_PLACE("deny_place"),
    ALLOW_BREAK("allow_break"),
    ALLOW_PLACE("allow_place");

    private final String name;
    private final Text mode_info;

    ZoneBlockMode(String name) {
        this.name = name;
        this.mode_info = Text.translatable("text.adventure_core.zone_block.mode_info." + name);
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Text asText() {
        return this.mode_info;
    }
}


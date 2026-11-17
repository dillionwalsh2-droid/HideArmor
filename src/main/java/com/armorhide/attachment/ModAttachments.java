package com.armorhide.attachment;

import com.armorhide.ArmorHide;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Server-persisted player hide flag. Synced to clients via custom packets
 * (NeoForge 1.21.1 attachments do not have built-in network sync).
 */
public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ArmorHide.MOD_ID);

    /**
     * When {@code true}, the player's worn armor models should not render.
     * Default {@code false} (armor visible). Copied on death so the preference sticks.
     */
    public static final Supplier<AttachmentType<Boolean>> ARMOR_HIDDEN = ATTACHMENT_TYPES.register(
            "armor_hidden",
            () -> AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL)
                    .copyOnDeath()
                    .build()
    );

    private ModAttachments() {
    }

    public static boolean isArmorHidden(net.minecraft.world.entity.player.Player player) {
        return player.getData(ARMOR_HIDDEN);
    }

    public static void setArmorHidden(net.minecraft.world.entity.player.Player player, boolean hidden) {
        player.setData(ARMOR_HIDDEN, hidden);
    }

    public static boolean toggleArmorHidden(net.minecraft.world.entity.player.Player player) {
        boolean next = !isArmorHidden(player);
        setArmorHidden(player, next);
        return next;
    }
}

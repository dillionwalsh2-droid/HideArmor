package com.armorhide.client;

import com.armorhide.ArmorHide;
import com.armorhide.attachment.ModAttachments;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModList;

import java.util.function.BooleanSupplier;

/**
 * Punchy draws armor on the first-person arms with its own renderer, bypassing
 * HumanoidArmorLayer. Accessed reflectively so Punchy stays an optional dependency.
 */
final class PunchyCompat {
    private static final String PUNCHY_MOD_ID = "punchy";

    private PunchyCompat() {
    }

    static void register() {
        if (!ModList.get().isLoaded(PUNCHY_MOD_ID)) {
            return;
        }
        BooleanSupplier hideArmor = () -> {
            var player = Minecraft.getInstance().player;
            return player != null && ModAttachments.isArmorHidden(player);
        };
        try {
            Class.forName("punchy.api.PunchyApi")
                    .getMethod("hideArmorOnArms", String.class, BooleanSupplier.class)
                    .invoke(null, ArmorHide.MOD_ID, hideArmor);
        } catch (ReflectiveOperationException | LinkageError e) {
            ArmorHide.LOGGER.warn("Failed to register first-person armor hiding with Punchy", e);
        }
    }
}

package com.armorhide.client;

import com.armorhide.ArmorHide;
import com.armorhide.attachment.ModAttachments;
import com.armorhide.network.ToggleArmorHidePayload;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod(value = ArmorHide.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ArmorHide.MOD_ID, value = Dist.CLIENT)
public class ArmorHideClient {
    public ArmorHideClient() {
    }

    @SubscribeEvent
    static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            addIconButton(event, screen);
        }
    }

    // Top-right corner inside the player preview box (+26,+8 .. +75,+78).
    private static final int ICON_OFFSET_X = 64;
    private static final int ICON_OFFSET_Y = 9;

    private static void addIconButton(ScreenEvent.Init.Post event, InventoryScreen screen) {
        var player = screen.getMinecraft().player;
        if (player == null) {
            return;
        }
        boolean hidden = ModAttachments.isArmorHidden(player);
        ArmorToggleButton button = new ArmorToggleButton(screen, ICON_OFFSET_X, ICON_OFFSET_Y, hidden, label(hidden), b -> {
            PacketDistributor.sendToServer(new ToggleArmorHidePayload());
            boolean next = !ModAttachments.isArmorHidden(player);
            ModAttachments.setArmorHidden(player, next);
            ArmorToggleButton self = (ArmorToggleButton) b;
            self.setArmorHidden(next);
            self.setMessage(label(next));
            self.setTooltip(Tooltip.create(label(next)));
        });
        button.setTooltip(Tooltip.create(label(hidden)));
        event.addListener(button);
    }

    private static Component label(boolean hidden) {
        return Component.translatable(hidden ? "armorhide.button.show" : "armorhide.button.hide");
    }
}
package com.armorhide.client;

import com.armorhide.ArmorHide;
import com.armorhide.attachment.ModAttachments;
import com.armorhide.network.ToggleArmorHidePayload;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
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
            addToggleButton(event, screen.getGuiLeft() + 126, screen.getGuiTop() + 58, screen.getMinecraft().player);
        } else if (event.getScreen() instanceof CreativeModeInventoryScreen screen) {
            // Place beside the inventory tab area, clear of the item grid.
            addToggleButton(event, screen.getGuiLeft() + 158, screen.getGuiTop() + 5, screen.getMinecraft().player);
        }
    }

    private static void addToggleButton(ScreenEvent.Init.Post event, int x, int y,
                                        net.minecraft.world.entity.player.Player player) {
        if (player == null) {
            return;
        }
        boolean hidden = ModAttachments.isArmorHidden(player);
        Button button = Button.builder(
                        label(hidden),
                        b -> {
                            PacketDistributor.sendToServer(new ToggleArmorHidePayload());
                            // Optimistic flip for snappy UI; server sync is authoritative.
                            boolean next = !ModAttachments.isArmorHidden(player);
                            ModAttachments.setArmorHidden(player, next);
                            b.setMessage(label(next));
                        })
                .bounds(x, y, 72, 20)
                .build();
        event.addListener(button);
    }

    private static Component label(boolean hidden) {
        return Component.translatable(hidden ? "armorhide.button.show" : "armorhide.button.hide");
    }
}

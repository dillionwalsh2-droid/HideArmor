package com.armorhide.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Compact 10x10 chestplate icon button, anchored to the container screen's GUI origin
 * so it follows the inventory when the recipe book shifts it.
 */
public class ArmorToggleButton extends Button {
    public static final int SIZE = 10;
    private static final ItemStack ICON = new ItemStack(Items.IRON_CHESTPLATE);

    private final AbstractContainerScreen<?> screen;
    private final int offsetX;
    private final int offsetY;
    private boolean armorHidden;

    public ArmorToggleButton(AbstractContainerScreen<?> screen, int offsetX, int offsetY,
                             boolean armorHidden, Component message, OnPress onPress) {
        super(screen.getGuiLeft() + offsetX, screen.getGuiTop() + offsetY, SIZE, SIZE, message, onPress, DEFAULT_NARRATION);
        this.screen = screen;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.armorHidden = armorHidden;
    }

    public void setArmorHidden(boolean armorHidden) {
        this.armorHidden = armorHidden;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        setX(screen.getGuiLeft() + offsetX);
        setY(screen.getGuiTop() + offsetY);
        int x = getX();
        int y = getY();

        int border = isHoveredOrFocused() ? 0xFFFFFFFF : 0xFF212121;
        graphics.fill(x, y, x + SIZE, y + SIZE, border);
        graphics.fill(x + 1, y + 1, x + SIZE - 1, y + SIZE - 1, 0xFF8B8B8B);

        graphics.pose().pushPose();
        graphics.pose().translate(x + 1, y + 1, 0);
        graphics.pose().scale(0.5f, 0.5f, 1.0f);
        graphics.renderFakeItem(ICON, 0, 0);
        graphics.pose().popPose();

        if (armorHidden) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 200);
            for (int i = 1; i < SIZE - 1; i++) {
                graphics.fill(x + i, y + SIZE - 1 - i, x + i + 1, y + SIZE - i, 0xFFE03030);
            }
            graphics.pose().popPose();
        }
    }
}

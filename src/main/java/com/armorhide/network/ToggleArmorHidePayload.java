package com.armorhide.network;

import com.armorhide.ArmorHide;
import com.armorhide.attachment.ModAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Client → server: request to flip the player's armor-hide flag.
 * The server is authoritative; clients never set the lasting value alone.
 */
public record ToggleArmorHidePayload() implements CustomPacketPayload {
    public static final Type<ToggleArmorHidePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ArmorHide.MOD_ID, "toggle"));

    public static final StreamCodec<ByteBuf, ToggleArmorHidePayload> STREAM_CODEC =
            StreamCodec.unit(new ToggleArmorHidePayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnServer(ToggleArmorHidePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            boolean hidden = ModAttachments.toggleArmorHidden(player);
            ArmorHideSync.syncToSelfAndTrackers(player);
            ArmorHide.LOGGER.debug("Player {} set armor hidden={}", player.getGameProfile().getName(), hidden);
        });
    }
}

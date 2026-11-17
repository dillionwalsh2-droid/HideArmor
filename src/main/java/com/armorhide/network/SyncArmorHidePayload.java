package com.armorhide.network;

import com.armorhide.ArmorHide;
import com.armorhide.attachment.ModAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Server → client: authoritative armor-hide state for a player entity.
 */
public record SyncArmorHidePayload(int entityId, boolean hidden) implements CustomPacketPayload {
    public static final Type<SyncArmorHidePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ArmorHide.MOD_ID, "sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncArmorHidePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, SyncArmorHidePayload::entityId,
                    ByteBufCodecs.BOOL, SyncArmorHidePayload::hidden,
                    SyncArmorHidePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(SyncArmorHidePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player local = context.player();
            if (local == null) {
                return;
            }
            Entity entity = local.level().getEntity(payload.entityId());
            if (entity instanceof Player player) {
                ModAttachments.setArmorHidden(player, payload.hidden());
            }
        });
    }
}

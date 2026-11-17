package com.armorhide.network;

import com.armorhide.attachment.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ArmorHideSync {
    private ArmorHideSync() {
    }

    public static void syncToSelfAndTrackers(ServerPlayer player) {
        boolean hidden = ModAttachments.isArmorHidden(player);
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                player,
                new SyncArmorHidePayload(player.getId(), hidden)
        );
    }

    public static void syncToPlayer(ServerPlayer recipient, ServerPlayer subject) {
        PacketDistributor.sendToPlayer(
                recipient,
                new SyncArmorHidePayload(subject.getId(), ModAttachments.isArmorHidden(subject))
        );
    }
}

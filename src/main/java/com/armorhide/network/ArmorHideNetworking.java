package com.armorhide.network;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ArmorHideNetworking {
    private ArmorHideNetworking() {
    }

    public static void register(PayloadRegistrar registrar) {
        registrar.playToServer(
                ToggleArmorHidePayload.TYPE,
                ToggleArmorHidePayload.STREAM_CODEC,
                ToggleArmorHidePayload::handleOnServer
        );
        registrar.playToClient(
                SyncArmorHidePayload.TYPE,
                SyncArmorHidePayload.STREAM_CODEC,
                SyncArmorHidePayload::handleOnClient
        );
    }
}

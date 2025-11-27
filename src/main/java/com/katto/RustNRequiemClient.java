package com.katto;

import com.katto.index.RustReqPackets;
import net.fabricmc.api.ClientModInitializer;

public class RustNRequiemClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RustReqPackets.registerClient();
    }
}

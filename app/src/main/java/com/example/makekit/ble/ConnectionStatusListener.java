package com.example.makekit.ble;

public interface ConnectionStatusListener {
    void connectionStatusChanged(boolean z);

    void serviceDiscoveryStatusChanged(boolean z);
}

package com.example.makekit.ble;

import android.bluetooth.BluetoothDevice;

public interface ScanResultsConsumer {
    void candidateBleDevice(BluetoothDevice bluetoothDevice, byte[] bArr, int i);

    void scanningStarted();

    void scanningStopped();
}

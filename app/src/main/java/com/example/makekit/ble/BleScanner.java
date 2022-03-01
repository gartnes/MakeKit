package com.example.makekit.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public abstract class BleScanner {
    BluetoothAdapter bluetooth_adapter = null;
    Context context;
    String device_name_start = "";
    Handler handler = new Handler();
    ScanResultsConsumer scan_results_consumer;
    boolean scanning = false;
    boolean select_bonded_devices_only = true;

    public abstract void startScanning(ScanResultsConsumer scanResultsConsumer);

    public abstract void startScanning(ScanResultsConsumer scanResultsConsumer, long j);

    public abstract void stopScanning();

    public BleScanner(Context context2) {
        this.context = context2;
        this.bluetooth_adapter = ((BluetoothManager) context2.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if (this.bluetooth_adapter == null || !this.bluetooth_adapter.isEnabled()) {
            Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context2.startActivity(enableBtIntent);
        }
    }

    public boolean isScanning() {
        return this.scanning;
    }

    /* access modifiers changed from: package-private */
    public void setScanning(boolean scanning2) {
        this.scanning = scanning2;
        if (!scanning2) {
            this.scan_results_consumer.scanningStopped();
        } else {
            this.scan_results_consumer.scanningStarted();
        }
    }

    public String getDevice_name_start() {
        return this.device_name_start;
    }

    public void setDevice_name_start(String device_name_start2) {
        this.device_name_start = device_name_start2;
    }

    public boolean isSelect_bonded_devices_only() {
        return this.select_bonded_devices_only;
    }

    public void setSelect_bonded_devices_only(boolean select_bonded_devices_only2) {
        this.select_bonded_devices_only = select_bonded_devices_only2;
    }
}

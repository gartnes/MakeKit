package com.example.makekit.ble;

import android.Manifest;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.example.makekit.microbit.Settings;

import java.util.ArrayList;
import java.util.List;

public class BleScannerAndroid12plus extends BleScanner {
    private static final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    /* access modifiers changed from: private */
    public ScanCallback scan_callback = new ScanCallback() {
        public void onScanResult(int callbackType, ScanResult result) {
            if (BleScannerAndroid12plus.this.scanning) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (BleScannerAndroid12plus.this.device_name_start != null && result.getDevice().getName() != null && !result.getDevice().getName().startsWith(BleScannerAndroid12plus.this.device_name_start)) {
                    return;
                }
                if (!BleScannerAndroid12plus.this.select_bonded_devices_only || !Settings.getInstance().isFilter_unpaired_devices() || result.getDevice().getBondState() == 12) {
                    BleScannerAndroid12plus.this.scan_results_consumer.candidateBleDevice(result.getDevice(), result.getScanRecord().getBytes(), result.getRssi());
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public BluetoothLeScanner scanner = null;

    public BleScannerAndroid12plus(Context context) {
        super(context);
    }

    public void startScanning(ScanResultsConsumer scan_results_consumer) {
        if (!this.scanning) {
            this.scan_results_consumer = scan_results_consumer;
            setScanning(true);
            scanLeDevices();
        }
    }

    public void startScanning(ScanResultsConsumer scan_results_consumer, long stop_after_ms) {
        if (!this.scanning) {
            if (this.scanner == null) {
                this.scanner = this.bluetooth_adapter.getBluetoothLeScanner();
            }
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    BleScannerAndroid12plus.this.scanner.stopScan(BleScannerAndroid12plus.this.scan_callback);
                    BleScannerAndroid12plus.this.setScanning(false);
                }
            }, stop_after_ms);
            startScanning(scan_results_consumer);
        }
    }

    public void stopScanning() {
        setScanning(false);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.scanner.stopScan(this.scan_callback);
    }

    private void scanLeDevices() {
        List<ScanFilter> filters = new ArrayList<>();
        ScanSettings settings = new ScanSettings.Builder().setScanMode(2).build();
        setScanning(true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.scanner.startScan(filters, settings, this.scan_callback);
    }
}

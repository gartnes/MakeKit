package com.example.makekit.ble;

import android.content.Context;
import android.os.Build;

public class BleHardwareScanner {
    public static BleScanner getBleScanner(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new BleScannerAndroid5plus(context);
        }
        return new BleScannerAndroid4(context);
    }
}

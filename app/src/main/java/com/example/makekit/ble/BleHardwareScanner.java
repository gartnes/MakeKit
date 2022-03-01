package com.example.makekit.ble;

import android.content.Context;
import android.os.Build;

public class BleHardwareScanner {
    public static BleScanner getBleScanner(Context context) {

        return new BleScannerAndroid5plus(context);

    }
}

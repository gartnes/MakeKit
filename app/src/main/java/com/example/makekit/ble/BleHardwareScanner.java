package com.example.makekit.ble;

import android.content.Context;
import android.os.Build;

public class BleHardwareScanner {
    public static BleScanner getBleScanner(Context context) {

        if (Build.VERSION.SDK_INT > 30){

            return new BleScannerAndroid12plus(context);
        } else{
            System.out.println(" ASDASF");
            return new BleScannerAndroid5plus(context);
        }


    }
}

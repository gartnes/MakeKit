package com.example.makekit;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

public class DeviceListActivity extends Activity {
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final String TAG = "DeviceListActivity";
    /* access modifiers changed from: private */
    public BluetoothAdapter mBtAdapter;
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            if (ActivityCompat.checkSelfPermission(DeviceListActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                DeviceListActivity.this.mBtAdapter.cancelDiscovery();
            }
            DeviceListActivity.this.mBtAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent intent = new Intent();
            intent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);
            DeviceListActivity.this.setResult(-1, intent);
            DeviceListActivity.this.finish();
        }
    };
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(5);
        setContentView(C0596R.layout.activity_ble_device_list);
        setResult(0);
        ((Button) findViewById(C0596R.C0598id.ScanButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setVisibility((int)8);
                DeviceListActivity.this.doDiscovery();
            }
        });
        ArrayAdapter<String> pairedDevicesArrayAdapter = new ArrayAdapter<>(this, C0596R.layout.activity_ble_device_list);
        this.mNewDevicesArrayAdapter = new ArrayAdapter<>(this, C0596R.layout.activity_ble_device_list);
        ListView pairedListView = (ListView) findViewById(C0596R.C0598id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(this.mDeviceClickListener);
        ListView newDevicesListView = (ListView) findViewById(C0596R.C0598id.new_devices);
        newDevicesListView.setAdapter(this.mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(this.mDeviceClickListener);
    }

    /* access modifiers changed from: private */
    public void doDiscovery() {
        setProgressBarIndeterminateVisibility(true);
        setTitle(C0596R.string.scanning);
        findViewById(C0596R.C0598id.title_new_devices).setVisibility((int)0);
    }
}

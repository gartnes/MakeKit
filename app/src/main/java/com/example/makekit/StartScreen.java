package com.example.makekit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class StartScreen extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 0;

    private TextView tv_pair;

    Button btn_bluetooth_on, btn_bluetooth_off, btn_discover_bluetooth, btn_paired_bluetooth_device;
    BluetoothAdapter bluetoothAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        tv_pair = findViewById(R.id.tv_pair);
        btn_bluetooth_on= findViewById(R.id.btn_bluetooth_on);
        btn_paired_bluetooth_device = findViewById(R.id.btn_paired_bluetooth_device);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        Button btn_airbit = findViewById(R.id.btn_airbit);

        btn_airbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartScreen.this, MainScreen.class);
                startActivity(intent);
            }
        });
        btn_bluetooth_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    showToast("Turning on Bluetooth..");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(StartScreen.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(intent, REQUEST_ENABLE_BT);
                    } else {
                        showToast("Bluetooth is already on");
                    }
                }

            }
        });


        btn_paired_bluetooth_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isEnabled()) {
                    tv_pair.setText("Paired Devices");
                    if (ActivityCompat.checkSelfPermission(StartScreen.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

                        for (BluetoothDevice device : devices) {
                            tv_pair.append("\n Device : " + device.getName() + " , " + device);
                        }
                    } else {
                        showToast("Turn on bluetooth to get paired devices");
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    showToast("Bluetooth is on");
                } else {
                    showToast("Bluetooth is off");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
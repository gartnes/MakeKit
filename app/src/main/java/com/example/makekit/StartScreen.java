package com.example.makekit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.makekit.ble.BleAdapterService;
import com.example.makekit.ble.BleHardwareScanner;
import com.example.makekit.ble.BleScanner;
import com.example.makekit.ble.ConnectionStatusListener;
import com.example.makekit.ble.ScanResultsConsumer;
import com.example.makekit.fragments.FragmentGamePad;
import com.example.makekit.fragments.FragmentSettings;
import com.example.makekit.fragments.FragmentWelcome;
import com.example.makekit.microbit.Constants;
import com.example.makekit.microbit.Microbit;
import com.example.makekit.microbit.MicrobitEvent;
import com.example.makekit.microbit.Settings;
import com.example.makekit.microbit.Utility;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class StartScreen extends AppCompatActivity implements ConnectionStatusListener, ScanResultsConsumer, NavigationView.OnNavigationItemSelectedListener, FragmentGamePad.GamePadListener {
    private static final String DEVICE_NAME_START = "BBC micro";
    private static String[] PERMISSIONS_LOCATION = {"android.permission.ACCESS_COARSE_LOCATION"};
    private static final long SCAN_TIMEOUT = 8000;
    String HELP_FRAG_TAG = "HELP TAG";
    String PAD_FRAG_TAG = "PAD TAG";
    String WEL_FRAG_TAG = "WEL TAG";

    //Fragments
    FragmentGamePad gamepadfragment;
    FragmentSettings settingsFragment;
    FragmentWelcome welcomefragment;

    //Bluetooth
    public ListAdapter ble_device_list_adapter;
    public BleScanner ble_scanner;
    public boolean ble_scanning = false;
    public BleAdapterService bluetooth_le_adapter;

    LinearLayout connectLayout;
    private int device_count = 0;
    String displayedFragment;
    private Vibrator gamepadVib;
    Intent gattServiceIntent;
    private Handler handler = new Handler();
    NavigationView navigationView;
    private boolean permissions_granted = false;
    public Toast toast;
    Toolbar toolbar;


    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(intent.getAction())) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (ActivityCompat.checkSelfPermission(StartScreen.this, Manifest.permission.BLUETOOTH_CONNECT) != 0) {
                    if (device.getBondState() == 10) {
                        StartScreen.this.showMsg(Utility.htmlColorRed("Device was not paired successfully"));
                    }

                } else if (ActivityCompat.checkSelfPermission(StartScreen.this, Manifest.permission.BLUETOOTH_CONNECT) != 0) {
                    if (device.getBondState() == 11) {
                        StartScreen.this.showMsg(Utility.htmlColorGreen("Pairing is in progress"));
                    }
                } else {
                    StartScreen.this.showMsg(Utility.htmlColorGreen("Device was paired successfully - select it now"));
                }
            }
        }
    };

    public Handler mMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    StartScreen.this.bluetooth_le_adapter.discoverServices();
                    return;
                case 2:
                    StartScreen.this.bluetooth_le_adapter.disconnect();
                    return;
                case 3:
                    for (BluetoothGattService svc : StartScreen.this.bluetooth_le_adapter.getSupportedGattServices()) {
                        Microbit.getInstance().addService(svc);
                    }
                    Microbit.getInstance().setMicrobit_services_discovered(true);
                    return;
                case 6:
                    msg.getData();
                    return;
                case 9:
                    Bundle bundle = msg.getData();
                    String service_uuid = bundle.getString(BleAdapterService.PARCEL_SERVICE_UUID);
                    String characteristic_uuid = bundle.getString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BleAdapterService unused = StartScreen.this.bluetooth_le_adapter = ((BleAdapterService.LocalBinder) service).getService();
            StartScreen.this.bluetooth_le_adapter.setActivityHandler(StartScreen.this.mMessageHandler);
            StartScreen.this.connectToDevice();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            BleAdapterService unused = StartScreen.this.bluetooth_le_adapter = null;
        }
    };

    static int access$1008(StartScreen x0) {
        int i = x0.device_count;
        x0.device_count = i + 1;
        return i;
    }

    static class ViewHolder {
        public TextView bdaddr;
        public TextView text;

        ViewHolder() {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        gamepadVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        connectLayout = (LinearLayout) findViewById(R.id.connect_layout);
        connectLayout.setVisibility(View.VISIBLE);
        welcomefragment = new FragmentWelcome();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, this.welcomefragment, this.WEL_FRAG_TAG).commit();
        displayedFragment = this.WEL_FRAG_TAG;
        Settings.getInstance().restore(this);
        ble_device_list_adapter = new ListAdapter();
        ListView listView = (ListView) findViewById(R.id.deviceList);
        listView.setAdapter(this.ble_device_list_adapter);
        registerReceiver(this.broadcastReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        ble_scanner = BleHardwareScanner.getBleScanner(getApplicationContext());
        ble_scanner.setDevice_name_start(DEVICE_NAME_START);
        ble_scanner.setSelect_bonded_devices_only(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (StartScreen.this.ble_scanning) {
                    StartScreen.this.setScanButton(0);
                    StartScreen.this.setScanState(false);
                    StartScreen.this.ble_scanner.stopScanning();
                }
                BluetoothDevice device = StartScreen.this.ble_device_list_adapter.getDevice(position);
                if (ActivityCompat.checkSelfPermission(StartScreen.this, Manifest.permission.BLUETOOTH_CONNECT) != 0) {
                }
                if (device.getBondState() != 10 || !Settings.getInstance().isFilter_unpaired_devices()) {
                    try {
                        StartScreen.this.unregisterReceiver(StartScreen.this.broadcastReceiver);
                    } catch (Exception e) {
                    }
                    if (StartScreen.this.toast != null) {
                        StartScreen.this.toast.cancel();
                    }
                    Microbit.getInstance().setBluetooth_device(device);
                    System.out.print(Microbit.getInstance().isMicrobit_connected());
                    Microbit.getInstance().setMicrobit_name(device.getName());
                    Microbit.getInstance().setMicrobit_address(device.getAddress());
                    Microbit.getInstance().setConnection_status_listener(StartScreen.this);
                    StartScreen.this.gattServiceIntent = new Intent(StartScreen.this, BleAdapterService.class);
                    StartScreen.this.bindService(StartScreen.this.gattServiceIntent, StartScreen.this.mServiceConnection, (int) 1);
                    return;
                }
                device.createBond();
                StartScreen.this.showMsg(Utility.htmlColorRed("Selected micro:bit must be paired - pairing now"));
            }
        });
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(this.broadcastReceiver);
        } catch (Exception e) {
        }
        Settings.getInstance().save(this);
    }

    public void passDpadPress(short microbitId, short microbitValue) {
        if (Microbit.getInstance().isMicrobit_connected()) {
            byte[] bArr = new byte[4];
            this.bluetooth_le_adapter.writeCharacteristic(Utility.normaliseUUID(BleAdapterService.EVENTSERVICE_SERVICE_UUID), Utility.normaliseUUID(BleAdapterService.CLIENTEVENT_CHARACTERISTIC_UUID), new MicrobitEvent(microbitId, microbitValue).getEventBytesForBle());
            this.gamepadVib.vibrate(50);
            return;
        }
        toast = Toast.makeText(this, "NOT CONNECTED: Please connect to Microbit from menu options", Toast.LENGTH_LONG);
        toast.show();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_gamepad) {
            gamepadfragment = new FragmentGamePad();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, gamepadfragment, PAD_FRAG_TAG).commit();
            displayedFragment = PAD_FRAG_TAG;
            connectLayout.setVisibility(View.GONE);

        } else if (id == R.id.nav_connect) {
            String str = this.displayedFragment;
            char c = 65535;
            switch (str.hashCode()) {
                case -1592582500:
                    if (str.equals("SET TAG")) {
                        c = 0;
                        break;
                    }
                    break;
                case -89419187:
                    if (str.equals("PAD TAG")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1950044056:
                    if (str.equals("WEL TAG")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getSupportFragmentManager().beginTransaction().hide(this.settingsFragment).commit();
                    this.connectLayout.setVisibility(View.INVISIBLE);
                    ((Button) findViewById(R.id.scanButton)).setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().hide(this.gamepadfragment).commit();
                    this.connectLayout.setVisibility(View.INVISIBLE);
                    ((Button) findViewById(R.id.scanButton)).setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().hide(this.welcomefragment).commit();
                    this.connectLayout.setVisibility(View.VISIBLE);
                    ((Button) findViewById(R.id.scanButton)).setVisibility(View.VISIBLE);
                    break;
                default:

                    break;
            }
            this.connectLayout.setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.scanButton)).setVisibility(View.VISIBLE);
            if (Microbit.getInstance().isMicrobit_connected()) {
                setScanButton(2);
            } else {
                setScanButton(0);
            }
        } else if (id == R.id.nav_help) {
            if (this.settingsFragment == null) {
                this.settingsFragment = new FragmentSettings();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, this.settingsFragment, this.HELP_FRAG_TAG).commit();
            this.displayedFragment = this.HELP_FRAG_TAG;
            this.connectLayout.setVisibility((int) 8);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onScan(View view) {
        if (Microbit.getInstance().isMicrobit_connected()) {
            unbindService(this.mServiceConnection);
            connectionStatusChanged(false);
            this.bluetooth_le_adapter.disconnect();
            setScanButton(0);
            return;
        }
        registerReceiver(this.broadcastReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        this.ble_scanner = BleHardwareScanner.getBleScanner(getApplicationContext());
        this.ble_scanner.setDevice_name_start(DEVICE_NAME_START);
        this.ble_scanner.setSelect_bonded_devices_only(true);
        if (!this.ble_scanner.isScanning()) {
            this.device_count = 0;
            if (Build.VERSION.SDK_INT < 31 && Build.VERSION.SDK_INT > 23 && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                this.permissions_granted = false;
                requestBlePermissions(StartScreen.this, 0);
                System.out.println("   TETATT");
            } else if (Build.VERSION.SDK_INT > 30 && checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                this.permissions_granted = false;
                requestBlePermissions(StartScreen.this, 0);
            } else {
                this.permissions_granted = true;
            }
            startScanning();
            return;
        }
        showMsg(Utility.htmlColorGreen("Stopping scanning"));
        this.ble_scanner.stopScanning();
    }

    private void startScanning() {
        if (this.permissions_granted) {
            runOnUiThread(new Runnable() {
                public void run() {
                    StartScreen.this.ble_device_list_adapter.clear();
                    StartScreen.this.ble_device_list_adapter.notifyDataSetChanged();
                }
            });
            simpleToast(getScanningMessage(), 2000);
            this.ble_scanner.startScanning(this, SCAN_TIMEOUT);
            return;
        }
        showMsg(Utility.htmlColorRed("Permission to perform Bluetooth scanning was not yet granted"));
    }

    public void connectToDevice() {
        showMsg(Utility.htmlColorBlue("Connecting to micro:bit"));
        if (this.bluetooth_le_adapter.connect(Microbit.getInstance().getMicrobit_address())) {
            setScanButton(2);
            return;
        }
        showMsg(Utility.htmlColorRed("onConnect: failed to connect"));
        setScanButton(0);
    }

    private static final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public static void requestBlePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
        else
            ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, requestCode);
    }

    @SuppressLint("ResourceType")
    private void generalAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) title);
        builder.setMessage((CharSequence) message);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        builder.show();
    }

    private void simpleToast(String message, int duration) {
        toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    public void setScanState(boolean value) {
        this.ble_scanning = value;
        ((Button) findViewById(R.id.scanButton)).setText(value ? Constants.STOP_SCANNING : getString(R.string.find_paired_microbits));
    }

    public void candidateBleDevice(final BluetoothDevice device, byte[] scan_record, int rssi) {
        runOnUiThread(new Runnable() {
            public void run() {
                StartScreen.this.ble_device_list_adapter.addDevice(device);
                StartScreen.this.ble_device_list_adapter.notifyDataSetChanged();
                StartScreen.access$1008(StartScreen.this);
            }
        });
    }

    public void scanningStarted() {
        setScanButton(1);
        setScanState(true);
        showMsg(Utility.htmlColorGreen(getScanningMessage()));
    }

    public void scanningStopped() {
        setScanButton(0);
        setScanState(false);
        if (this.device_count > 0) {
            showMsg(Utility.htmlColorGreen(getString(R.string.click_on_microbit_to_connect)));
        } else {
            showMsg(Utility.htmlColorRed(getNoneFoundMessage()));
        }
    }

    public void setScanButton(int buttonState) {
        switch (buttonState) {
            case 0:
                if (Settings.getInstance().isFilter_unpaired_devices()) {
                    ((TextView) findViewById(R.id.scanButton)).setText(R.string.scan_for_microbit);
                    return;
                } else {
                    ((TextView) findViewById(R.id.scanButton)).setText(R.string.scan_for_paired_microbits);
                    return;
                }
            case 1:
                ((TextView) findViewById(R.id.scanButton)).setText(R.string.stop_scanning);
                return;
            case 2:
                ((TextView) findViewById(R.id.scanButton)).setText(R.string.disconnect);
                return;
            default:
                return;
        }
    }

    private class ListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> ble_devices = new ArrayList<>();

        public ListAdapter() {
        }

        public void addDevice(BluetoothDevice device) {
            if (!this.ble_devices.contains(device)) {
                this.ble_devices.add(device);
            }
        }

        public boolean contains(BluetoothDevice device) {
            return this.ble_devices.contains(device);
        }

        public BluetoothDevice getDevice(int position) {
            return this.ble_devices.get(position);
        }

        public void clear() {
            this.ble_devices.clear();
        }

        public int getCount() {
            return this.ble_devices.size();
        }

        public Object getItem(int i) {
            return this.ble_devices.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = StartScreen.this.getLayoutInflater().inflate(R.layout.list_row, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.text = view.findViewById(R.id.textView);
                viewHolder.bdaddr = view.findViewById(R.id.bdaddr);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice device = this.ble_devices.get(i);
            if (ActivityCompat.checkSelfPermission(StartScreen.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestBlePermissions(StartScreen.this, 0);
            }
            String deviceName = device.getName();
            if (device.getBondState() == 12) {
                deviceName = deviceName + " (BONDED)";
            }
            if (deviceName == null || deviceName.length() <= 0) {
                viewHolder.text.setText("unknown device");
            } else {
                viewHolder.text.setText(deviceName);
            }
            viewHolder.bdaddr.setText(device.getAddress());
            return view;
        }
    }


    public void showMsg(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                ((TextView) StartScreen.this.findViewById(R.id.message)).setText(Html.fromHtml(msg));
            }
        });
    }

    private String getScanningMessage() {
        if (Settings.getInstance().isFilter_unpaired_devices()) {
            return getString(R.string.scanning_for_paired_microbits);
        }
        return getString(R.string.scanning_for_all_microbits);
    }

    private String getNoneFoundMessage() {
        if (Settings.getInstance().isFilter_unpaired_devices()) {
            return Constants.NO_PAIRED_FOUND;
        }
        return Constants.NONE_FOUND;
    }

    public void connectionStatusChanged(boolean connected) {
        if (connected) {
            showMsg(Utility.htmlColorGreen(getString(R.string.connected)));
        } else {
            showMsg(Utility.htmlColorRed(getString(R.string.disconnected)));
        }
    }

    public void serviceDiscoveryStatusChanged(boolean new_state) {
    }
}
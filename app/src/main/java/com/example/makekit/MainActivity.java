package com.example.makekit;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.content.pm.PackageManager;
import android.net.Uri;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.example.makekit.MicrobitEvent;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ConnectionStatusListener, ScanResultsConsumer, NavigationView.OnNavigationItemSelectedListener, FragmentGamePad.GamePadListener {
    public static final int BLE_CONNECTED = 2;
    public static final int BLE_DISCONNECTED_READY = 0;
    public static final int BLE_SCANNING = 1;
    private static final String DEVICE_NAME_START = "BBC micro";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_NAME = "name";
    private static String[] PERMISSIONS_LOCATION = {"android.permission.ACCESS_COARSE_LOCATION"};
    private static final int REQUEST_LOCATION = 0;
    private static final long SCAN_TIMEOUT = 8000;
    String BLE_FRAG_TAG = "BLE TAG";
    String HELP_FRAG_TAG = "HELP TAG";
    String PAD_FRAG_TAG = "PAD TAG";
    String SET_FRAG_TAG = "SET TAG";
    String WEL_FRAG_TAG = "WEL TAG";
    /* access modifiers changed from: private */
    public ListAdapter ble_device_list_adapter;
    /* access modifiers changed from: private */
    public BleScanner ble_scanner;
    /* access modifiers changed from: private */
    public boolean ble_scanning = false;
    /* access modifiers changed from: private */
    public BleAdapterService bluetooth_le_adapter;
    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(intent.getAction())) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (device.getBondState() == 10) {
                        MainActivity.this.showMsg(Utility.htmlColorRed("Device was not paired successfully"));
                    }

                } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (device.getBondState() == 11) {
                        MainActivity.this.showMsg(Utility.htmlColorGreen("Pairing is in progress"));
                    }
                } else {
                    MainActivity.this.showMsg(Utility.htmlColorGreen("Device was paired successfully - select it now"));
                }
            }
        }
    };
    BluetoothAdapter btAdapter;
    BluetoothManager btManager;
    LinearLayout connectLayout;
    private int device_count = 0;
    String displayedFragment;
    private Vibrator gamepadVib;
    FragmentGamePad gamepadfragment;
    Intent gattServiceIntent;
    private Handler handler = new Handler();
    FragmentHelp helpfragment;
    /* access modifiers changed from: private */
    public Handler mMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MainActivity.this.bluetooth_le_adapter.discoverServices();
                    return;
                case 2:
                    MainActivity.this.bluetooth_le_adapter.disconnect();
                    return;
                case 3:
                    for (BluetoothGattService svc : MainActivity.this.bluetooth_le_adapter.getSupportedGattServices()) {
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
            BleAdapterService unused = MainActivity.this.bluetooth_le_adapter = ((BleAdapterService.LocalBinder) service).getService();
            MainActivity.this.bluetooth_le_adapter.setActivityHandler(MainActivity.this.mMessageHandler);
            MainActivity.this.connectToDevice();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            BleAdapterService unused = MainActivity.this.bluetooth_le_adapter = null;
        }
    };
    NavigationView navigationView;
    private boolean permissions_granted = false;
    FragmentSettings settingsfragment;
    /* access modifiers changed from: private */
    public Toast toast;
    Toolbar toolbar;
    FragmentWelcome welcomefragment;

    static /* synthetic */ int access$1008(MainActivity x0) {
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

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_start_screen);
        this.gamepadVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.navigationView = (NavigationView) findViewById(C0596R.C0598id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);
        this.connectLayout = (LinearLayout) findViewById(C0596R.C0598id.connect_layout);
        this.connectLayout.setVisibility((int) 8);
        this.welcomefragment = new FragmentWelcome();
        getSupportFragmentManager().beginTransaction().replace(C0596R.C0598id.fragment_area, this.welcomefragment, this.WEL_FRAG_TAG).commit();
        this.displayedFragment = this.WEL_FRAG_TAG;
        Settings.getInstance().restore(this);
        this.ble_device_list_adapter = new ListAdapter();
        ListView listView = (ListView) findViewById(C0596R.C0598id.deviceList);
        listView.setAdapter(this.ble_device_list_adapter);
        registerReceiver(this.broadcastReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        this.ble_scanner = BleHardwareScanner.getBleScanner(getApplicationContext());
        this.ble_scanner.setDevice_name_start(DEVICE_NAME_START);
        this.ble_scanner.setSelect_bonded_devices_only(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (MainActivity.this.ble_scanning) {
                    MainActivity.this.setScanButton(0);
                    MainActivity.this.setScanState(false);
                    MainActivity.this.ble_scanner.stopScanning();
                }
                BluetoothDevice device = MainActivity.this.ble_device_list_adapter.getDevice(position);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (device.getBondState() != 10 || !Settings.getInstance().isFilter_unpaired_devices()) {
                        try {
                            MainActivity.this.unregisterReceiver(MainActivity.this.broadcastReceiver);
                        } catch (Exception e) {
                        }
                        if (MainActivity.this.toast != null) {
                            MainActivity.this.toast.cancel();
                        }
                        Microbit.getInstance().setBluetooth_device(device);
                        Microbit.getInstance().setMicrobit_name(device.getName());
                        Microbit.getInstance().setMicrobit_address(device.getAddress());
                        Microbit.getInstance().setConnection_status_listener(MainActivity.this);
                        MainActivity.this.gattServiceIntent = new Intent(MainActivity.this, BleAdapterService.class);
                        MainActivity.this.bindService(MainActivity.this.gattServiceIntent, MainActivity.this.mServiceConnection, (int) 1);
                        return;
                    }

                }
                device.createBond();
                MainActivity.this.showMsg(Utility.htmlColorRed("Selected micro:bit must be paired - pairing now"));
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

    /* access modifiers changed from: protected */
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
        this.toast = Toast.makeText(this, "NOT CONNECTED: Please connect to Microbit from menu options", (int) 0);
        this.toast.show();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0596R.C0598id.nav_gamepad) {
            this.gamepadfragment = new FragmentGamePad();
            getSupportFragmentManager().beginTransaction().replace(C0596R.C0598id.fragment_area, this.gamepadfragment, this.PAD_FRAG_TAG).commit();
            this.displayedFragment = this.PAD_FRAG_TAG;
            this.connectLayout.setVisibility((int) 8);
        } else if (id == C0596R.C0598id.nav_connect) {
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
                        c = 2;
                        break;
                    }
                    break;
                case -7729637:
                    if (str.equals("HELP TAG")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1950044056:
                    if (str.equals("WEL TAG")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getSupportFragmentManager().beginTransaction().hide(this.settingsfragment).commit();
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().hide(this.helpfragment).commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().hide(this.gamepadfragment).commit();
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction().hide(this.welcomefragment).commit();
                    break;
                default:
                    this.connectLayout.setVisibility((int) 0);
                    break;
            }
            this.connectLayout.setVisibility((int) 0);
            if (Microbit.getInstance().isMicrobit_connected()) {
                setScanButton(2);
            } else {
                setScanButton(0);
            }
        } else if (id == C0596R.C0598id.nav_help) {
            if (this.helpfragment == null) {
                this.helpfragment = new FragmentHelp();
            }
            getSupportFragmentManager().beginTransaction().replace(C0596R.C0598id.fragment_area, this.helpfragment, this.HELP_FRAG_TAG).commit();
            this.displayedFragment = this.HELP_FRAG_TAG;
            this.connectLayout.setVisibility((int) 8);
        } else if (id == C0596R.C0598id.nav_website) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.kitronik.co.uk")));
        } else if (id == C0596R.C0598id.nav_facebook) {
            startActivity(getOpenFacebookIntent(this));
        } else if (id == C0596R.C0598id.nav_twitter) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://twitter.com/Kitronik")));
        } else if (id == C0596R.C0598id.nav_youtube) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.youtube.com/user/Kitronik")));
        }
        ((DrawerLayout) findViewById(C0596R.C0598id.drawer_layout)).closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent("android.intent.action.VIEW", Uri.parse("fb://page/145744082219006"));
        } catch (Exception e) {
            return new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/Kitronik"));
        }
    }

    /* access modifiers changed from: protected */
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
            if (Build.VERSION.SDK_INT < 23) {
                this.permissions_granted = true;
            } else if (checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != (int) 0) {
                this.permissions_granted = false;
                requestLocationPermission();
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
                    MainActivity.this.ble_device_list_adapter.clear();
                    MainActivity.this.ble_device_list_adapter.notifyDataSetChanged();
                }
            });
            simpleToast(getScanningMessage(), 2000);
            this.ble_scanner.startScanning(this, SCAN_TIMEOUT);
            return;
        }
        showMsg(Utility.htmlColorRed("Permission to perform Bluetooth scanning was not yet granted"));
    }

    /* access modifiers changed from: private */
    public void connectToDevice() {
        showMsg(Utility.htmlColorBlue("Connecting to micro:bit"));
        if (this.bluetooth_le_adapter.connect(Microbit.getInstance().getMicrobit_address())) {
            setScanButton(2);
            return;
        }
        showMsg(Utility.htmlColorRed("onConnect: failed to connect"));
        setScanButton(0);
    }

    @SuppressLint("ResourceType")
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_COARSE_LOCATION")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) "Permission Required");
            builder.setMessage((CharSequence) "Please grant Location access so this application can perform Bluetooth scanning");
            builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 0);
                }
            });
            builder.show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 0);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 0) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (grantResults.length == 1 && grantResults[0] == 0) {
            this.permissions_granted = true;
            if (this.ble_scanner.isScanning()) {
                startScanning();
            }
        }
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
        this.toast = Toast.makeText(this, message, duration);
        this.toast.show();
    }

    /* access modifiers changed from: private */
    public void setScanState(boolean value) {
        this.ble_scanning = value;
        ((Button) findViewById(C0596R.C0598id.scanButton)).setText(value ? Constants.STOP_SCANNING : "Find paired BBC micro:bits");
    }

    public void candidateBleDevice(final BluetoothDevice device, byte[] scan_record, int rssi) {
        runOnUiThread(new Runnable() {
            public void run() {
                MainActivity.this.ble_device_list_adapter.addDevice(device);
                MainActivity.this.ble_device_list_adapter.notifyDataSetChanged();
                MainActivity.access$1008(MainActivity.this);
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
            showMsg(Utility.htmlColorGreen("Click on microbit in list to connect"));
        } else {
            showMsg(Utility.htmlColorRed(getNoneFoundMessage()));
        }
    }

    public void setScanButton(int buttonState) {
        switch (buttonState) {
            case 0:
                if (Settings.getInstance().isFilter_unpaired_devices()) {
                    ((TextView) findViewById(C0596R.C0598id.scanButton)).setText(Constants.SCAN_FOR_MICROBIT);
                    return;
                } else {
                    ((TextView) findViewById(C0596R.C0598id.scanButton)).setText(Constants.SCAN_FOR_PAIRED_MICROBIT);
                    return;
                }
            case 1:
                ((TextView) findViewById(C0596R.C0598id.scanButton)).setText(Constants.STOP_SCANNING);
                return;
            case 2:
                ((TextView) findViewById(C0596R.C0598id.scanButton)).setText(Constants.DISCONNECT);
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
                view = MainActivity.this.getLayoutInflater().inflate(C0596R.layout.list_row, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) view.findViewById(C0596R.C0598id.textView);
                viewHolder.bdaddr = (TextView) view.findViewById(C0596R.C0598id.bdaddr);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice device = this.ble_devices.get(i);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
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
            return view;
        }
    }

    /* access modifiers changed from: private */
    public void showMsg(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                ((TextView) MainActivity.this.findViewById(C0596R.C0598id.message)).setText(Html.fromHtml(msg));
            }
        });
    }

    private String getScanningMessage() {
        if (Settings.getInstance().isFilter_unpaired_devices()) {
            return "Scanning for paired micro:bits";
        }
        return "Scanning for all micro:bits";
    }

    private String getNoneFoundMessage() {
        if (Settings.getInstance().isFilter_unpaired_devices()) {
            return Constants.NO_PAIRED_FOUND;
        }
        return Constants.NONE_FOUND;
    }

    public void connectionStatusChanged(boolean connected) {
        if (connected) {
            showMsg(Utility.htmlColorGreen("Connected"));
        } else {
            showMsg(Utility.htmlColorRed("Disconnected"));
        }
    }

    public void serviceDiscoveryStatusChanged(boolean new_state) {
    }
}

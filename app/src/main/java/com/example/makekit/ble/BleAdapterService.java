package com.example.makekit.ble;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.core.app.ActivityCompat;

import com.example.makekit.microbit.Constants;
import com.example.makekit.microbit.Microbit;
import com.example.makekit.microbit.Utility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;

public class BleAdapterService extends Service implements Runnable {
    public static String ACCELEROMETERDATA_CHARACTERISTIC_UUID = "E95DCA4B251D470AA062FA1922DFA9A8";
    public static String ACCELEROMETERPERIOD_CHARACTERISTIC_UUID = "E95DFB24251D470AA062FA1922DFA9A8";
    public static String ACCELEROMETERSERVICE_SERVICE_UUID = "E95D0753251D470AA062FA1922DFA9A8";
    public static String APPEARANCE_CHARACTERISTIC_UUID = "00002A0100001000800000805F9B34FB";
    public static String BUTTON1STATE_CHARACTERISTIC_UUID = "E95DDA90251D470AA062FA1922DFA9A8";
    public static String BUTTON2STATE_CHARACTERISTIC_UUID = "E95DDA91251D470AA062FA1922DFA9A8";
    public static String BUTTONSERVICE_SERVICE_UUID = "E95D9882251D470AA062FA1922DFA9A8";
    public static String CLIENTEVENT_CHARACTERISTIC_UUID = "E95D5404251D470AA062FA1922DFA9A8";
    public static String CLIENTREQUIREMENTS_CHARACTERISTIC_UUID = "E95D23C4251D470AA062FA1922DFA9A8";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String DEVICEINFORMATION_SERVICE_UUID = "0000180A00001000800000805F9B34FB";
    public static String DEVICENAME_CHARACTERISTIC_UUID = "00002A0000001000800000805F9B34FB";
    public static String DFUCONTROLSERVICE_SERVICE_UUID = "E95D93B0251D470AA062FA1922DFA9A8";
    public static String DFUCONTROL_CHARACTERISTIC_UUID = "E95D93B1251D470AA062FA1922DFA9A8";
    public static String EVENTSERVICE_SERVICE_UUID = "E95D93AF251D470AA062FA1922DFA9A8";
    public static String FIRMWAREREVISIONSTRING_CHARACTERISTIC_UUID = "00002A2600001000800000805F9B34FB";
    public static final int GATT_CHARACTERISTIC_READ = 4;
    public static final int GATT_CHARACTERISTIC_WRITTEN = 9;
    public static final int GATT_CONNECTED = 1;
    public static final int GATT_DESCRIPTOR_WRITTEN = 10;
    public static final int GATT_DISCONNECT = 2;
    public static final int GATT_REMOTE_RSSI = 5;
    public static final int GATT_SERVICES_DISCOVERED = 3;
    public static String GENERICACCESS_SERVICE_UUID = "0000180000001000800000805F9B34FB";
    public static String GENERICATTRIBUTE_SERVICE_UUID = "0000180100001000800000805F9B34FB";
    public static String HARDWAREREVISIONSTRING_CHARACTERISTIC_UUID = "00002A2700001000800000805F9B34FB";
    public static String HEARTRATEMEASUREMENT_CHARACTERISTIC_UUID = "00002a3700001000800000805F9B34FB";
    public static String HEARTRATE_SERVICE_16_BIT_UUID = "180D";
    public static String HEARTRATE_SERVICE_UUID = "0000180D00001000800000805F9B34FB";
    public static String IOPINSERVICE_SERVICE_UUID = "E95D127B251D470AA062FA1922DFA9A8";
    public static String LEDMATRIXSTATE_CHARACTERISTIC_UUID = "E95D7B77251D470AA062FA1922DFA9A8";
    public static String LEDSERVICE_SERVICE_UUID = "E95DD91D251D470AA062FA1922DFA9A8";
    public static String LEDTEXT_CHARACTERISTIC_UUID = "E95D93EE251D470AA062FA1922DFA9A8";
    public static String MAGNETOMETERBEARING_CHARACTERISTIC_UUID = "E95D9715251D470AA062FA1922DFA9A8";
    public static String MAGNETOMETERDATA_CHARACTERISTIC_UUID = "E95DFB11251D470AA062FA1922DFA9A8";
    public static String MAGNETOMETERPERIOD_CHARACTERISTIC_UUID = "E95D386C251D470AA062FA1922DFA9A8";
    public static String MAGNETOMETERSERVICE_SERVICE_UUID = "E95DF2D8251D470AA062FA1922DFA9A8";
    public static String MANUFACTURERNAMESTRING_CHARACTERISTIC_UUID = "00002A2900001000800000805F9B34FB";
    public static final int MESSAGE = 6;
    public static String MICROBITEVENT_CHARACTERISTIC_UUID = "E95D9775251D470AA062FA1922DFA9A8";
    public static String MICROBITREQUIREMENTS_CHARACTERISTIC_UUID = "E95DB84C251D470AA062FA1922DFA9A8";
    public static String MODELNUMBERSTRING_CHARACTERISTIC_UUID = "00002A2400001000800000805F9B34FB";
    public static final int NOTIFICATION_OR_INDICATION_RECEIVED = 7;
    public static final String PARCEL_CHARACTERISTIC_UUID = "CHARACTERISTIC_UUID";
    public static final String PARCEL_DESCRIPTOR_UUID = "DESCRIPTOR_UUID";
    public static final String PARCEL_RSSI = "RSSI";
    public static final String PARCEL_SERVICE_UUID = "SERVICE_UUID";
    public static final String PARCEL_TEXT = "TEXT";
    public static final String PARCEL_VALUE = "VALUE";
    public static String PINADCONFIGURATION_CHARACTERISTIC_UUID = "E95D5899251D470AA062FA1922DFA9A8";
    public static String PINDATA_CHARACTERISTIC_UUID = "E95D8D00251D470AA062FA1922DFA9A8";
    public static String PINIOCONFIGURATION_CHARACTERISTIC_UUID = "E95DB9FE251D470AA062FA1922DFA9A8";
    public static String SCROLLINGDELAY_CHARACTERISTIC_UUID = "E95D0D2D251D470AA062FA1922DFA9A8";
    public static String SERIALNUMBERSTRING_CHARACTERISTIC_UUID = "00002A2500001000800000805F9B34FB";
    public static String SERVICECHANGED_CHARACTERISTIC_UUID = "2A05";
    public static final int SIMULATED_NOTIFICATION_RECEIVED = 8;
    public static String TEMPERATURESERVICE_SERVICE_UUID = "E95D6100251D470AA062FA1922DFA9A8";
    public static String TEMPERATURE_CHARACTERISTIC_UUID = "E95D9250251D470AA062FA1922DFA9A8";
    public static String UARTSERVICE_SERVICE_UUID = "6E400001B5A3F393E0A9E50E24DCCA9E";
    public static String UART_RX_CHARACTERISTIC_UUID = "6E400003B5A3F393E0A9E50E24DCCA9E";
    public static String UART_TX_CHARACTERISTIC_UUID = "6E400002B5A3F393E0A9E50E24DCCA9E";
    /* access modifiers changed from: private */
    public Handler activity_handler = null;
    private BluetoothAdapter bluetooth_adapter;
    /* access modifiers changed from: private */
    public BluetoothGatt bluetooth_gatt;
    private BluetoothManager bluetooth_manager;
    private BluetoothGattDescriptor descriptor;
    private BluetoothDevice device;
    private KeepAlive keep_alive = new KeepAlive();
    private final IBinder mBinder = new LocalBinder();
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            BleAdapterService.this.timestamp();
            if (newState == 2) {
                Microbit.getInstance().setMicrobit_connected(true);
                Message.obtain(BleAdapterService.this.activity_handler, 1).sendToTarget();
            } else if (newState == 0) {
                Microbit.getInstance().setMicrobit_connected(false);
                Message.obtain(BleAdapterService.this.activity_handler, 2).sendToTarget();
                if (BleAdapterService.this.bluetooth_gatt != null) {
                    if (ActivityCompat.checkSelfPermission(BleAdapterService.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        BleAdapterService.this.bluetooth_gatt.close();
                        BluetoothGatt unused = BleAdapterService.this.bluetooth_gatt = null;
                    }
                    BleAdapterService.this.bluetooth_gatt.close();
                    BluetoothGatt unused = BleAdapterService.this.bluetooth_gatt = null;
                }
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BleAdapterService.this.timestamp();
            Message.obtain(BleAdapterService.this.activity_handler, 3).sendToTarget();
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            BleAdapterService.this.timestamp();
            if (status == 0) {
                Bundle bundle = new Bundle();
                bundle.putString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID, characteristic.getUuid().toString());
                bundle.putString(BleAdapterService.PARCEL_SERVICE_UUID, characteristic.getService().getUuid().toString());
                bundle.putByteArray(BleAdapterService.PARCEL_VALUE, characteristic.getValue());
                Message msg = Message.obtain(BleAdapterService.this.activity_handler, 4);
                msg.setData(bundle);
                msg.sendToTarget();
            } else {
                BleAdapterService.this.sendConsoleMessage("characteristic read err:" + status);
            }
            BleAdapterService.this.operationCompleted();
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            BleAdapterService.this.timestamp();
            if (status == 0) {
                Bundle bundle = new Bundle();
                bundle.putString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID, characteristic.getUuid().toString());
                bundle.putString(BleAdapterService.PARCEL_SERVICE_UUID, characteristic.getService().getUuid().toString());
                bundle.putByteArray(BleAdapterService.PARCEL_VALUE, characteristic.getValue());
                Message msg = Message.obtain(BleAdapterService.this.activity_handler, 9);
                msg.setData(bundle);
                msg.sendToTarget();
            } else {
                BleAdapterService.this.sendConsoleMessage("characteristic write err:" + status);
            }
            BleAdapterService.this.operationCompleted();
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            BleAdapterService.this.timestamp();
            Bundle bundle = new Bundle();
            bundle.putString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID, characteristic.getUuid().toString());
            bundle.putString(BleAdapterService.PARCEL_SERVICE_UUID, characteristic.getService().getUuid().toString());
            bundle.putByteArray(BleAdapterService.PARCEL_VALUE, characteristic.getValue());
            Message msg = Message.obtain(BleAdapterService.this.activity_handler, 7);
            msg.setData(bundle);
            msg.sendToTarget();
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            BleAdapterService.this.timestamp();
            if (status == 0) {
                Bundle bundle = new Bundle();
                bundle.putString(BleAdapterService.PARCEL_DESCRIPTOR_UUID, descriptor.getUuid().toString());
                bundle.putString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID, descriptor.getCharacteristic().getUuid().toString());
                bundle.putString(BleAdapterService.PARCEL_SERVICE_UUID, descriptor.getCharacteristic().getService().toString());
                bundle.putByteArray(BleAdapterService.PARCEL_VALUE, descriptor.getValue());
                Message msg = Message.obtain(BleAdapterService.this.activity_handler, 10);
                msg.setData(bundle);
                msg.sendToTarget();
            } else {
                BleAdapterService.this.sendConsoleMessage("characteristic write err:" + status);
            }
            BleAdapterService.this.operationCompleted();
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == 0) {
                Bundle bundle = new Bundle();
                bundle.putInt(BleAdapterService.PARCEL_RSSI, rssi);
                Message msg = Message.obtain(BleAdapterService.this.activity_handler, 5);
                msg.setData(bundle);
                msg.sendToTarget();
                return;
            }
            BleAdapterService.this.sendConsoleMessage("RSSI read err:" + status);
        }
    };
    private Object mutex = new Object();
    private ArrayList<Operation> operation_queue = new ArrayList<>();
    private boolean request_processor_running = false;
    /* access modifiers changed from: private */
    public long timestamp;

    public BluetoothDevice getDevice() {
        return this.device;
    }

    /* access modifiers changed from: private */
    public void timestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public void startRequestProcessor() {
        Thread t = new Thread(this);
        this.request_processor_running = true;
        t.start();
    }

    public void stopRequestProcessor() {
        this.request_processor_running = false;
        addOperation(new Operation(-1));
    }

    private boolean isRequestInProgress() {
        boolean busy;
        synchronized (this.mutex) {
            busy = this.operation_queue.size() > 0;
        }
        return busy;
    }

    /* access modifiers changed from: private */
    public void addOperation(Operation op) {
        synchronized (this.mutex) {
            while (this.operation_queue.size() > 0) {
                try {
                    this.mutex.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.operation_queue.size() == 0) {
                this.operation_queue.add(op);
                this.mutex.notifyAll();
            }
        }
    }

    /* access modifiers changed from: private */
    public void operationCompleted() {
        synchronized (this.mutex) {
            if (this.operation_queue.size() > 0) {
                this.operation_queue.remove(0);
                this.mutex.notifyAll();
            }
        }
    }

    private void emptyOperationQueue() {
        synchronized (this.mutex) {
            if (this.operation_queue.size() > 0) {
                this.operation_queue.clear();
                this.mutex.notifyAll();
            }
        }
    }

    public boolean isRequest_processor_running() {
        return this.request_processor_running;
    }

    public void setRequest_processor_running(boolean request_processor_running2) {
        this.request_processor_running = request_processor_running2;
    }

    public void run() {
        Operation current_op = null;
        while (this.request_processor_running) {
            try {
                synchronized (this.mutex) {
                    while (true) {
                        if (this.operation_queue.size() == 0 || this.operation_queue.get(0).getOperation_status() == 1) {
                            try {
                                if (this.operation_queue.size() > 0) {
                                }
                                this.mutex.wait(Constants.GATT_OPERATION_TIME_OUT);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (this.operation_queue.size() > 0) {
                            Operation op = this.operation_queue.get(0);
                            if (current_op == op) {
                                sendConsoleMessage("Previous operation timed out");
                                operationCompleted();
                            } else {
                                op.setOperation_status(1);
                                current_op = op;
                                boolean ok = false;
                                switch (op.getOperation_type()) {
                                    case -1:
                                        ok = true;
                                        this.operation_queue.remove(0);
                                        break;
                                    case 1:
                                        ok = executeReadCharacteristic(op.getService_uuid(), op.getCharacteristic_uuid());
                                        break;
                                    case 2:
                                        ok = executeWriteCharacteristic(op.getService_uuid(), op.getCharacteristic_uuid(), op.getValue());
                                        break;
                                    case 3:
                                        ok = executeSetCccdState(op.getService_uuid(), op.getCharacteristic_uuid(), op.isSubscribe(), op.getValue());
                                        break;
                                }
                                if (!ok) {
                                    sendConsoleMessage("Error: GATT operation failed");
                                    operationCompleted();
                                }
                                this.mutex.notifyAll();
                            }
                        }
                    }
                }
            } catch (Exception e2) {
                sendConsoleMessage("ERROR: serious problem in Bluetooth request processor");
            }
        }
        emptyOperationQueue();
    }

    class KeepAlive implements Runnable {
        private int frequency = 10;
        boolean running = false;

        KeepAlive() {
        }

        public void start() {
            new Thread(this).start();
        }

        public void stop() {
            this.running = false;
        }

        public void run() {
            this.running = true;
            try {
                Thread.sleep(((long) Math.random()) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (this.running) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                if (this.running && System.currentTimeMillis() - BleAdapterService.this.timestamp > 10000 && Microbit.getInstance().isMicrobit_connected()) {
                    BleAdapterService.this.readCharacteristic(Utility.normaliseUUID(BleAdapterService.DEVICEINFORMATION_SERVICE_UUID), Utility.normaliseUUID(BleAdapterService.FIRMWAREREVISIONSTRING_CHARACTERISTIC_UUID));
                }
            }
        }
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public BleAdapterService getService() {
            return BleAdapterService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        startRequestProcessor();
        this.keep_alive.start();
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void onCreate() {
        if (this.bluetooth_manager == null) {
            this.bluetooth_manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (this.bluetooth_manager == null) {
                return;
            }
        }
        this.bluetooth_adapter = this.bluetooth_manager.getAdapter();
        if (this.bluetooth_adapter == null) {
        }
    }

    public boolean connect(String address) {
        if (this.bluetooth_adapter == null || address == null) {
            sendConsoleMessage("connect: bluetooth_adapter=null");
            return false;
        }
        this.device = this.bluetooth_adapter.getRemoteDevice(address);
        if (this.device == null) {
            sendConsoleMessage("connect: device=null");
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            this.bluetooth_gatt = this.device.connectGatt(this, false, this.mGattCallback);
            return true;

        }
        this.bluetooth_gatt = this.device.connectGatt(this, false, this.mGattCallback);
        return true;
    }

    public void disconnect() {
        sendConsoleMessage("disconnecting");
        if (this.bluetooth_adapter == null || this.bluetooth_gatt == null) {
            sendConsoleMessage("disconnect: bluetooth_adapter|bluetooth_gatt null");
            return;
        }
        stopRequestProcessor();
        this.keep_alive.stop();
        if (this.bluetooth_gatt != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                this.bluetooth_gatt.disconnect();
            }
            this.bluetooth_gatt.disconnect();
        }
    }

    public void discoverServices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            this.bluetooth_gatt.discoverServices();
        }
        this.bluetooth_gatt.discoverServices();
    }

    public void setActivityHandler(Handler handler) {
        this.activity_handler = handler;
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.bluetooth_gatt == null) {
            return null;
        }
        return this.bluetooth_gatt.getServices();
    }

    public boolean readCharacteristic(String serviceUuid, String characteristicUuid) {
        if (!Microbit.getInstance().isMicrobit_connected()) {
            return true;
        }
        if (this.bluetooth_adapter == null || this.bluetooth_gatt == null) {
            sendConsoleMessage("readCharacteristic: bluetooth_adapter|bluetooth_gatt null");
            return false;
        }
        BluetoothGattService gattService = this.bluetooth_gatt.getService(UUID.fromString(serviceUuid));
        if (gattService == null) {
            sendConsoleMessage("readCharacteristic: gattService null");
            return false;
        } else if (gattService.getCharacteristic(UUID.fromString(characteristicUuid)) == null) {
            sendConsoleMessage("readCharacteristic: gattChar null");
            return false;
        } else {
            final Operation op = new Operation(1, serviceUuid, characteristicUuid);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                public void run() {
                    BleAdapterService.this.addOperation(op);
                }
            });
            return true;
        }
    }

    private boolean executeReadCharacteristic(String serviceUuid, String characteristicUuid) {
        if (!Microbit.getInstance().isMicrobit_connected()) {
            return true;
        }
        if (this.bluetooth_adapter == null || this.bluetooth_gatt == null) {
            sendConsoleMessage("readCharacteristic: bluetooth_adapter|bluetooth_gatt null");
            return false;
        }
        BluetoothGattService gattService = this.bluetooth_gatt.getService(UUID.fromString(serviceUuid));
        if (gattService == null) {
            sendConsoleMessage("readCharacteristic: gattService null");
            return false;
        }
        BluetoothGattCharacteristic gattChar = gattService.getCharacteristic(UUID.fromString(characteristicUuid));
        if (gattChar == null) {
            sendConsoleMessage("readCharacteristic: gattChar null");
            return false;
        }
        timestamp();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return this.bluetooth_gatt.readCharacteristic(gattChar);
        }
        return this.bluetooth_gatt.readCharacteristic(gattChar);
    }

    public boolean writeCharacteristic(String serviceUuid, String characteristicUuid, byte[] value) {
        if (!Microbit.getInstance().isMicrobit_connected()) {
            return true;
        }
        if (this.bluetooth_adapter == null || this.bluetooth_gatt == null) {
            sendConsoleMessage("writeCharacteristic: bluetooth_adapter|bluetooth_gatt null");
            return false;
        }
        BluetoothGattService gattService = this.bluetooth_gatt.getService(UUID.fromString(serviceUuid));
        if (gattService == null) {
            sendConsoleMessage("writeCharacteristic: gattService null");
            return false;
        } else if (gattService.getCharacteristic(UUID.fromString(characteristicUuid)) == null) {
            sendConsoleMessage("writeCharacteristic: gattChar null");
            return false;
        } else {
            final Operation op = new Operation(2, serviceUuid, characteristicUuid, value);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                public void run() {
                    BleAdapterService.this.addOperation(op);
                }
            });
            return true;
        }
    }

    private boolean executeWriteCharacteristic(String serviceUuid, String characteristicUuid, byte[] value) {
        if (!Microbit.getInstance().isMicrobit_connected()) {
            return true;
        }
        if (this.bluetooth_adapter == null || this.bluetooth_gatt == null) {
            sendConsoleMessage("writeCharacteristic: bluetooth_adapter|bluetooth_gatt null");
            return false;
        }
        BluetoothGattService gattService = this.bluetooth_gatt.getService(UUID.fromString(serviceUuid));
        if (gattService == null) {
            sendConsoleMessage("writeCharacteristic: gattService null");
            return false;
        }
        BluetoothGattCharacteristic gattChar = gattService.getCharacteristic(UUID.fromString(characteristicUuid));
        if (gattChar == null) {
            sendConsoleMessage("writeCharacteristic: gattChar null");
            return false;
        }
        gattChar.setValue(value);
        timestamp();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return this.bluetooth_gatt.writeCharacteristic(gattChar);

        }
        return this.bluetooth_gatt.writeCharacteristic(gattChar);
    }

    public boolean setNotificationsState(String serviceUuid, String characteristicUuid, boolean enabled) {
        if (!Microbit.getInstance().isMicrobit_connected()) {
            return true;
        }
        if (this.bluetooth_adapter == null || this.bluetooth_gatt == null) {
            sendConsoleMessage("setNotificationsState: bluetooth_adapter|bluetooth_gatt null");
            return false;
        }
        BluetoothGattService gattService = this.bluetooth_gatt.getService(UUID.fromString(serviceUuid));
        if (gattService == null) {
            sendConsoleMessage("setNotificationsState: gattService null");
            return false;
        } else if (gattService.getCharacteristic(UUID.fromString(characteristicUuid)) == null) {
            sendConsoleMessage("setNotificationsState: gattChar null");
            return false;
        } else {
            byte[] cccd_value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
            if (!enabled) {
                cccd_value = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
            }
            final Operation op = new Operation(3, serviceUuid, characteristicUuid, enabled, cccd_value);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                public void run() {
                    BleAdapterService.this.addOperation(op);
                }
            });
            return true;
        }
    }

    public boolean setIndicationsState(String serviceUuid, String characteristicUuid, boolean enabled) {
        if (!Microbit.getInstance().isMicrobit_connected()) {
            return true;
        }
        if (this.bluetooth_adapter == null || this.bluetooth_gatt == null) {
            sendConsoleMessage("setIndicationsState: bluetooth_adapter|bluetooth_gatt null");
            return false;
        }
        BluetoothGattService gattService = this.bluetooth_gatt.getService(UUID.fromString(serviceUuid));
        if (gattService == null) {
            sendConsoleMessage("setIndicationsState: gattService null");
            return false;
        } else if (gattService.getCharacteristic(UUID.fromString(characteristicUuid)) == null) {
            sendConsoleMessage("setIndicationsState: gattChar null");
            return false;
        } else {
            byte[] cccd_value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
            if (!enabled) {
                cccd_value = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
            }
            final Operation op = new Operation(3, serviceUuid, characteristicUuid, enabled, cccd_value);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                public void run() {
                    BleAdapterService.this.addOperation(op);
                }
            });
            return true;
        }
    }

    private boolean executeSetCccdState(String serviceUuid, String characteristicUuid, boolean enabled, byte[] cccd_enable) {
        if (!Microbit.getInstance().isMicrobit_connected()) {
            return true;
        }
        if (this.bluetooth_adapter == null || this.bluetooth_gatt == null) {
            sendConsoleMessage("executeSetNotificationsState: bluetooth_adapter|bluetooth_gatt null");
            return false;
        }
        BluetoothGattService gattService = this.bluetooth_gatt.getService(UUID.fromString(serviceUuid));
        if (gattService == null) {
            sendConsoleMessage("executeSetNotificationsState: gattService null");
            return false;
        }
        BluetoothGattCharacteristic gattChar = gattService.getCharacteristic(UUID.fromString(characteristicUuid));
        if (gattChar == null) {
            sendConsoleMessage("executeSetNotificationsState: gattChar null");
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            this.bluetooth_gatt.setCharacteristicNotification(gattChar, enabled);

        }
        this.bluetooth_gatt.setCharacteristicNotification(gattChar, enabled);
        this.descriptor = gattChar.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
        if (enabled) {
            this.descriptor.setValue(cccd_enable);
        } else {
            this.descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        timestamp();
        return this.bluetooth_gatt.writeDescriptor(this.descriptor);
    }

    public void readRemoteRssi() {
        if (this.bluetooth_adapter != null && this.bluetooth_gatt != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                this.bluetooth_gatt.readRemoteRssi();
            }
            this.bluetooth_gatt.readRemoteRssi();
        }
    }

    /* access modifiers changed from: private */
    public void sendConsoleMessage(String text) {
        Message msg = Message.obtain(this.activity_handler, 6);
        Bundle data = new Bundle();
        data.putString(PARCEL_TEXT, text);
        msg.setData(data);
        msg.sendToTarget();
    }

    public boolean refreshDeviceCache() {
        try {
            Method refresh_method = this.bluetooth_gatt.getClass().getMethod("refresh", new Class[0]);
            if (refresh_method != null) {
                return ((Boolean) refresh_method.invoke(this.bluetooth_gatt, new Object[0])).booleanValue();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
package com.example.makekit.microbit;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.example.makekit.ble.Handle;
import com.example.makekit.ble.ConnectionStatusListener;
import java.util.Hashtable;
import java.util.List;

public class Microbit {
    private static Microbit instance;
    private BluetoothDevice bluetooth_device;
    private ConnectionStatusListener connection_status_listener;
    private String microbit_address;
    private boolean microbit_connected;
    private String microbit_name;
    private boolean microbit_services_discovered;
    private Hashtable<Handle, List<BluetoothGattCharacteristic>> service_characteristics;
    private Hashtable<Handle, BluetoothGattService> services;

    private Microbit() {
        resetAttributeTables();
    }

    public static synchronized Microbit getInstance() {
        Microbit microbit;
        synchronized (Microbit.class) {
            if (instance == null) {
                instance = new Microbit();
                System.out.print("MicroBit instance created");
            }
            microbit = instance;
        }
        return microbit;
    }

    public void resetAttributeTables() {
        this.services = new Hashtable<>();
        this.service_characteristics = new Hashtable<>();
    }

    public BluetoothDevice getBluetooth_device() {
        return this.bluetooth_device;
    }

    public void setBluetooth_device(BluetoothDevice bluetooth_device2) {
        this.bluetooth_device = bluetooth_device2;
    }

    public String getMicrobit_name() {
        return this.microbit_name;
    }

    public void setMicrobit_name(String microbit_name2) {
        this.microbit_name = microbit_name2;
    }

    public String getMicrobit_address() {
        return this.microbit_address;
    }

    public void setMicrobit_address(String microbit_address2) {
        this.microbit_address = microbit_address2;
    }

    public boolean isMicrobit_connected() {
        return this.microbit_connected;
    }

    public void setMicrobit_connected(boolean microbit_connected2) {
        this.microbit_connected = microbit_connected2;
        if (this.connection_status_listener != null) {
            this.connection_status_listener.connectionStatusChanged(microbit_connected2);
        }
    }

    public boolean isMicrobit_services_discovered() {
        return this.microbit_services_discovered;
    }

    public void setMicrobit_services_discovered(boolean microbit_services_discovered2) {
        this.microbit_services_discovered = microbit_services_discovered2;
        if (this.connection_status_listener != null) {
            this.connection_status_listener.serviceDiscoveryStatusChanged(microbit_services_discovered2);
        }
    }

    public ConnectionStatusListener getConnection_status_listener() {
        return this.connection_status_listener;
    }

    public void setConnection_status_listener(ConnectionStatusListener connection_status_listener2) {
        this.connection_status_listener = connection_status_listener2;
    }

    public void addService(BluetoothGattService svc) {
        this.services.put(new Handle(svc.getUuid(), svc.getInstanceId()), svc);
        this.service_characteristics.put(new Handle(svc.getUuid(), svc.getInstanceId()), svc.getCharacteristics());
    }

    public boolean hasService(String service_uuid) {
        String svc_uuid = Utility.normaliseUUID(service_uuid).toLowerCase();
        for (BluetoothGattService service : this.services.values()) {
            if (service.getUuid().toString().equalsIgnoreCase(svc_uuid)) {
                return true;
            }
        }
        return false;
    }
}

package com.example.makekit.ble;

import com.example.makekit.microbit.Utility;

public class Operation {
    public static final int OPERATION_EXECUTING = 1;
    public static final int OPERATION_EXIT_QUEUE_PROCESSING_REQUEST = -1;
    public static final int OPERATION_PENDING = 0;
    public static final int OPERATION_READ_CHARACTERISTIC_REQUEST = 1;
    public static final int OPERATION_WRITE_CHARACTERISTIC_REQUEST = 2;
    public static final int OPERATION_WRITE_DESCRIPTOR_REQUEST = 3;
    private String characteristic_uuid;
    private String descriptor_uuid;
    private int operation_status;
    private int operation_type;
    private String service_uuid;
    private boolean subscribe = false;
    private byte[] value;

    public Operation(int operation_type2, String service_uuid2, String characteristic_uuid2, String descriptor_uuid2, byte[] value2) {
        this.operation_type = operation_type2;
        this.service_uuid = service_uuid2;
        this.characteristic_uuid = characteristic_uuid2;
        this.descriptor_uuid = descriptor_uuid2;
        this.value = value2;
    }

    public Operation(int operation_type2, String service_uuid2, String characteristic_uuid2) {
        this.operation_type = operation_type2;
        this.service_uuid = service_uuid2;
        this.characteristic_uuid = characteristic_uuid2;
    }

    public Operation(int operation_type2, String service_uuid2, String characteristic_uuid2, byte[] value2) {
        this.operation_type = operation_type2;
        this.service_uuid = service_uuid2;
        this.characteristic_uuid = characteristic_uuid2;
        this.value = value2;
    }

    public Operation(int operation_type2, String service_uuid2, String characteristic_uuid2, boolean subscribe2, byte[] value2) {
        this.operation_type = operation_type2;
        this.service_uuid = service_uuid2;
        this.characteristic_uuid = characteristic_uuid2;
        this.subscribe = subscribe2;
        this.value = value2;
    }

    public Operation(int operation_type2) {
        this.operation_type = operation_type2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operation operation = (Operation) o;
        if (this.operation_type != operation.operation_type || !this.service_uuid.equals(operation.service_uuid) || !this.characteristic_uuid.equals(operation.characteristic_uuid)) {
            return false;
        }
        return this.descriptor_uuid.equals(operation.descriptor_uuid);
    }

    public int hashCode() {
        return (((((this.operation_type * 31) + this.service_uuid.hashCode()) * 31) + this.characteristic_uuid.hashCode()) * 31) + this.descriptor_uuid.hashCode();
    }

    public String toString() {
        return "Operation{operation_type=" + this.operation_type + ", operation_status=" + this.operation_status + ", service_uuid='" + this.service_uuid + '\'' + ", characteristic_uuid='" + this.characteristic_uuid + '\'' + ", descriptor_uuid='" + this.descriptor_uuid + '\'' + ", value=" + Utility.byteArrayAsHexString(this.value) + '}';
    }

    public int getOperation_type() {
        return this.operation_type;
    }

    public void setOperation_type(int operation_type2) {
        this.operation_type = operation_type2;
    }

    public String getService_uuid() {
        return this.service_uuid;
    }

    public void setService_uuid(String service_uuid2) {
        this.service_uuid = service_uuid2;
    }

    public String getCharacteristic_uuid() {
        return this.characteristic_uuid;
    }

    public void setCharacteristic_uuid(String characteristic_uuid2) {
        this.characteristic_uuid = characteristic_uuid2;
    }

    public String getDescriptor_uuid() {
        return this.descriptor_uuid;
    }

    public void setDescriptor_uuid(String descriptor_uuid2) {
        this.descriptor_uuid = descriptor_uuid2;
    }

    public int getOperation_status() {
        return this.operation_status;
    }

    public void setOperation_status(int operation_status2) {
        this.operation_status = operation_status2;
    }

    public byte[] getValue() {
        return this.value;
    }

    public void setValue(byte[] value2) {
        this.value = value2;
    }

    public boolean isSubscribe() {
        return this.subscribe;
    }

    public void setSubscribe(boolean subscribe2) {
        this.subscribe = subscribe2;
    }
}

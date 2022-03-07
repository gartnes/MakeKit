package com.example.makekit.microbit;

public class MicrobitEvent {
    private short event_type;
    private short event_value;

    public MicrobitEvent(short event_type2, short event_value2) {
        this.event_type = event_type2;
        this.event_value = event_value2;
    }

    public MicrobitEvent(byte[] event_bytes) {
        byte[] event_type_bytes = new byte[2];
        byte[] event_value_bytes = new byte[2];
        System.arraycopy(event_bytes, 0, event_type_bytes, 0, 2);
        System.arraycopy(event_bytes, 2, event_value_bytes, 0, 2);
        this.event_type = Utility.shortFromLittleEndianBytes(event_type_bytes);
        this.event_value = Utility.shortFromLittleEndianBytes(event_value_bytes);
    }

    public short getEvent_type() {
        return this.event_type;
    }

    public void setEvent_type(short event_type2) {
        this.event_type = event_type2;
    }

    public short getEvent_value() {
        return this.event_value;
    }

    public void setEvent_value(short event_value2) {
        this.event_value = event_value2;
    }

    public byte[] getEventBytesForBle() {
        byte[] bArr = new byte[2];
        byte[] bArr2 = new byte[2];
        byte[] event_bytes = new byte[4];
        byte[] event_type_bytes = Utility.leBytesFromShort(this.event_type);
        byte[] event_value_bytes = Utility.leBytesFromShort(this.event_value);
        System.arraycopy(event_type_bytes, 0, event_bytes, 0, 2);
        System.arraycopy(event_value_bytes, 0, event_bytes, 2, 2);
        return event_bytes;
    }
}

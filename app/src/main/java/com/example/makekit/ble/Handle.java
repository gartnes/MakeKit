package com.example.makekit.ble;

import java.util.UUID;

public class Handle {
    private int instance_id;
    private UUID uuid;

    public Handle(UUID uuid2, int instance_id2) {
        this.uuid = uuid2;
        this.instance_id = instance_id2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Handle handle = (Handle) o;
        if (this.instance_id != handle.instance_id) {
            return false;
        }
        if (this.uuid != null) {
            if (this.uuid.equals(handle.uuid)) {
                return true;
            }
        } else if (handle.uuid == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((this.uuid != null ? this.uuid.hashCode() : 0) * 31) + this.instance_id;
    }

    public String toString() {
        return "Handle{uuid=" + this.uuid + ", instance_id=" + this.instance_id + '}';
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid2) {
        this.uuid = uuid2;
    }

    public int getInstance_id() {
        return this.instance_id;
    }

    public void setInstance_id(int instance_id2) {
        this.instance_id = instance_id2;
    }
}

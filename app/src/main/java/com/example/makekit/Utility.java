package com.example.makekit;

public class Utility {
    static final float ALPHA = 0.15f;
    private static final String BLUETOOTH_SIG_UUID_BASE = "0000XXXX-0000-1000-8000-00805f9b34fb";
    private static final String HEX_CHARS = "01234567890ABCDEF";
    public static final String TAG = "BBC microbit";

    public static float[] lowPass(float[] input, float[] output) {
        if (output == null) {
            return input;
        }
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + (ALPHA * (input[i] - output[i]));
        }
        return output;
    }

    public static String normaliseUUID(String uuid) {
        String normalised_128_bit_uuid = uuid;
        if (uuid.length() == 4) {
            normalised_128_bit_uuid = BLUETOOTH_SIG_UUID_BASE.replace("XXXX", uuid);
        }
        if (uuid.length() == 32) {
            return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
        }
        return normalised_128_bit_uuid;
    }

    public static String extractCharacteristicUuidFromTag(String tag) {
        String[] parts = tag.split("_");
        if (parts.length == 4) {
            return parts[3];
        }
        return "";
    }

    public static short shortFromLittleEndianBytes(byte[] bytes) {
        if (bytes == null || bytes.length != 2) {
            return 0;
        }
        short result = (short) ((bytes[0] & 255) + ((bytes[1] & 255) << 8));
        if ((result | 32768) == 32768) {
            return (short) (result * -1);
        }
        return result;
    }

    public static short shortFromLittleEndianBytes(byte b0, byte b1) {
        short result = (short) (((b1 & 255) << 8) + (b0 & 255));
        if ((result | 32768) == 32768) {
            return (short) (result * -1);
        }
        return result;
    }

    public static int intFromLittleEndianBytes(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            return 0;
        }
        int result = (bytes[3] << 24) + (bytes[2] << 16) + (bytes[1] << 8) + bytes[0];
        if ((result | Integer.MIN_VALUE) == Integer.MIN_VALUE) {
            return result * -1;
        }
        return result;
    }

    public static String extractServiceUuidFromTag(String tag) {
        String[] parts = tag.split("_");
        if (parts.length == 4) {
            return parts[2];
        }
        return "";
    }

    public static byte[] getByteArrayFromHexString(String hex_string) {
        String hex = hex_string.replace(" ", "").toUpperCase();
        byte[] bytes = new byte[(hex.length() / 2)];
        int i = 0;
        int j = 0;
        while (i < hex.length()) {
            String h1 = hex.substring(i, i + 1);
            String h2 = hex.substring(i + 1, i + 2);
            try {
                int j2 = j + 1;
                try {
                    bytes[j] = (byte) ((Integer.valueOf(h1, 16).intValue() * 16) + Integer.valueOf(h2, 16).intValue());
                    i += 2;
                    j = j2;
                } catch (NumberFormatException e) {
                    e = e;
                    int i2 = j2;
                    System.out.println("NFE handling " + h1 + h2 + " with i=" + i);
                    throw e;
                }
            } catch (NumberFormatException e2) {
                e2 = e2;
                System.out.println("NFE handling " + h1 + h2 + " with i=" + i);
                throw e2;
            }
        }
        return bytes;
    }

    public static String byteArrayAsHexString(byte[] bytes) {
        boolean z;
        if (bytes == null) {
            return "[null]";
        }
        int l = bytes.length;
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < l; i++) {
            if (bytes[i] >= 0) {
                z = true;
            } else {
                z = false;
            }
            if (z && (bytes[i] < 16)) {
                hex.append("0");
            }
            hex.append(Integer.toString(bytes[i] & 255, 16).toUpperCase());
        }
        return hex.toString();
    }

    public static byte[] leBytesFromShort(short s) {
        return new byte[]{(byte) (s & 255), (byte) ((s >> 8) & 255)};
    }

    public static byte[] leBytesFromTwoShorts(short s1, short s2) {
        return new byte[]{(byte) (s1 & 255), (byte) ((s1 >> 8) & 255), (byte) (s2 & 255), (byte) ((s2 >> 8) & 255)};
    }

    public static byte[] leBytesFromInt(int s) {
        return new byte[]{(byte) (s & 255), (byte) ((s >> 8) & 255), (byte) ((s >> 16) & 255), (byte) ((s >> 24) & 255)};
    }

    public static byte[] beBytesFromInt(int s) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (s & 255);
        bytes[2] = (byte) ((s >> 8) & 255);
        bytes[1] = (byte) ((s >> 16) & 255);
        bytes[0] = (byte) ((s >> 24) & 255);
        return bytes;
    }

    public static boolean isValidHex(String hex_string) {
        System.out.println("isValidHex(" + hex_string + ")");
        String hex = hex_string.replace(" ", "").toUpperCase();
        int len = hex.length();
        if (len % 2 != 0) {
            System.out.println("isValidHex: not even number of chars");
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (!HEX_CHARS.contains(hex.substring(i, i + 1))) {
                return false;
            }
        }
        return true;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = HEX_CHARS.toCharArray();
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public static String htmlColorRed(String s) {
        return "<font color=\"#FF0000\">" + s + "</font>";
    }

    public static String htmlColorGreen(String s) {
        return "<font color=\"#009933\">" + s + "</font>";
    }

    public static String htmlColorBlue(String s) {
        return "<font color=\"#3300CC\">" + s + "</font> ";
    }

    public static void main(String[] args) {
        System.out.println(byteArrayAsHexString(leBytesFromShort((short) 15)));
        System.out.println(byteArrayAsHexString(leBytesFromShort((short) 256)));
        System.out.println(byteArrayAsHexString(leBytesFromTwoShorts((short) 15, (short)256)));
    }

    public static int toggleVisibility(int current_visibility) {
        if (current_visibility == 4) {
            return 0;
        }
        return 4;
    }
}

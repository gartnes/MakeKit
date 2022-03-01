package com.example.makekit;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private static final String FILTER_UNPAIRED_DEVICES = "filter_unpaired_devices";
    private static final String MES_DPAD_1_BUTTON_DOWN_OFF = "mes_dpad_1_button_down_off";
    private static final String MES_DPAD_1_BUTTON_DOWN_ON = "mes_dpad_1_button_down_on";
    private static final String MES_DPAD_1_BUTTON_LEFT_OFF = "mes_dpad_1_button_left_off";
    private static final String MES_DPAD_1_BUTTON_LEFT_ON = "mes_dpad_1_button_left_on";
    private static final String MES_DPAD_1_BUTTON_RIGHT_OFF = "mes_dpad_1_button_right_off";
    private static final String MES_DPAD_1_BUTTON_RIGHT_ON = "mes_dpad_1_button_right_on";
    private static final String MES_DPAD_1_BUTTON_UP_OFF = "mes_dpad_1_button_up_off";
    private static final String MES_DPAD_1_BUTTON_UP_ON = "mes_dpad_1_button_up_on";
    private static final String MES_DPAD_2_BUTTON_DOWN_OFF = "mes_dpad_2_button_down_off";
    private static final String MES_DPAD_2_BUTTON_DOWN_ON = "mes_dpad_2_button_down_on";
    private static final String MES_DPAD_2_BUTTON_LEFT_OFF = "mes_dpad_2_button_left_off";
    private static final String MES_DPAD_2_BUTTON_LEFT_ON = "mes_dpad_2_button_left_on";
    private static final String MES_DPAD_2_BUTTON_RIGHT_OFF = "mes_dpad_2_button_right_off";
    private static final String MES_DPAD_2_BUTTON_RIGHT_ON = "mes_dpad_2_button_right_on";
    private static final String MES_DPAD_2_BUTTON_UP_OFF = "mes_dpad_2_button_up_off";
    private static final String MES_DPAD_2_BUTTON_UP_ON = "mes_dpad_2_button_up_on";
    private static final String MES_DPAD_CONTROLLER = "mes_dpad_controller";
    private static final String SCROLLING_DELAY = "scrolling_delay";
    private static final String SETTINGS_FILE = "com.kitronik.kitronikmove.settings_file";
    private static Settings instance;
    private boolean filter_unpaired_devices = true;
    private short mes_dpad_1_button_down_off = 4;
    private short mes_dpad_1_button_down_on = 3;
    private short mes_dpad_1_button_left_off = 6;
    private short mes_dpad_1_button_left_on = 5;
    private short mes_dpad_1_button_right_off = 8;
    private short mes_dpad_1_button_right_on = 7;
    private short mes_dpad_1_button_up_off = 2;
    private short mes_dpad_1_button_up_on = 1;
    private short mes_dpad_2_button_down_off = 12;
    private short mes_dpad_2_button_down_on = 11;
    private short mes_dpad_2_button_left_off = 14;
    private short mes_dpad_2_button_left_on = 13;
    private short mes_dpad_2_button_right_off = 16;
    private short mes_dpad_2_button_right_on = 15;
    private short mes_dpad_2_button_up_off = 10;
    private short mes_dpad_2_button_up_on = 9;
    private short mes_dpad_controller = 1104;
    private short scrolling_delay = 500;

    private Settings() {
    }

    public static synchronized Settings getInstance() {
        Settings settings;
        synchronized (Settings.class) {
            if (instance == null) {
                instance = new Settings();
            }
            settings = instance;
        }
        return settings;
    }

    public void save(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTINGS_FILE, 0).edit();
        editor.putBoolean(FILTER_UNPAIRED_DEVICES, this.filter_unpaired_devices);
        editor.putInt(MES_DPAD_CONTROLLER, this.mes_dpad_controller);
        editor.putInt(MES_DPAD_1_BUTTON_UP_ON, this.mes_dpad_1_button_up_on);
        editor.putInt(MES_DPAD_1_BUTTON_UP_OFF, this.mes_dpad_1_button_up_off);
        editor.putInt(MES_DPAD_1_BUTTON_DOWN_ON, this.mes_dpad_1_button_down_on);
        editor.putInt(MES_DPAD_1_BUTTON_DOWN_OFF, this.mes_dpad_1_button_down_off);
        editor.putInt(MES_DPAD_1_BUTTON_LEFT_ON, this.mes_dpad_1_button_left_on);
        editor.putInt(MES_DPAD_1_BUTTON_LEFT_OFF, this.mes_dpad_1_button_left_off);
        editor.putInt(MES_DPAD_1_BUTTON_RIGHT_ON, this.mes_dpad_1_button_right_on);
        editor.putInt(MES_DPAD_1_BUTTON_RIGHT_OFF, this.mes_dpad_1_button_right_off);
        editor.putInt(MES_DPAD_2_BUTTON_UP_ON, this.mes_dpad_2_button_up_on);
        editor.putInt(MES_DPAD_2_BUTTON_UP_OFF, this.mes_dpad_2_button_up_off);
        editor.putInt(MES_DPAD_2_BUTTON_DOWN_ON, this.mes_dpad_2_button_down_on);
        editor.putInt(MES_DPAD_2_BUTTON_DOWN_OFF, this.mes_dpad_2_button_down_off);
        editor.putInt(MES_DPAD_2_BUTTON_LEFT_ON, this.mes_dpad_2_button_left_on);
        editor.putInt(MES_DPAD_2_BUTTON_LEFT_OFF, this.mes_dpad_2_button_left_off);
        editor.putInt(MES_DPAD_2_BUTTON_RIGHT_ON, this.mes_dpad_2_button_right_on);
        editor.putInt(MES_DPAD_2_BUTTON_RIGHT_OFF, this.mes_dpad_2_button_right_off);
        editor.commit();
    }

    public void restore(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SETTINGS_FILE, 0);
        this.scrolling_delay = (short) sharedPref.getInt(SCROLLING_DELAY, 500);
        this.filter_unpaired_devices = sharedPref.getBoolean(FILTER_UNPAIRED_DEVICES, true);
        this.mes_dpad_controller = (short) sharedPref.getInt(MES_DPAD_CONTROLLER, 1104);
        this.mes_dpad_1_button_up_on = (short) sharedPref.getInt(MES_DPAD_1_BUTTON_UP_ON, 1);
        this.mes_dpad_1_button_up_off = (short) sharedPref.getInt(MES_DPAD_1_BUTTON_UP_OFF, 2);
        this.mes_dpad_1_button_down_on = (short) sharedPref.getInt(MES_DPAD_1_BUTTON_DOWN_ON, 3);
        this.mes_dpad_1_button_down_off = (short) sharedPref.getInt(MES_DPAD_1_BUTTON_DOWN_OFF, 4);
        this.mes_dpad_1_button_left_on = (short) sharedPref.getInt(MES_DPAD_1_BUTTON_LEFT_ON, 5);
        this.mes_dpad_1_button_left_off = (short) sharedPref.getInt(MES_DPAD_1_BUTTON_LEFT_OFF, 6);
        this.mes_dpad_1_button_right_on = (short) sharedPref.getInt(MES_DPAD_1_BUTTON_RIGHT_ON, 7);
        this.mes_dpad_1_button_right_off = (short) sharedPref.getInt(MES_DPAD_1_BUTTON_RIGHT_OFF, 8);
        this.mes_dpad_2_button_up_on = (short) sharedPref.getInt(MES_DPAD_2_BUTTON_UP_ON, 9);
        this.mes_dpad_2_button_up_off = (short) sharedPref.getInt(MES_DPAD_2_BUTTON_UP_OFF, 10);
        this.mes_dpad_2_button_down_on = (short) sharedPref.getInt(MES_DPAD_2_BUTTON_DOWN_ON, 11);
        this.mes_dpad_2_button_down_off = (short) sharedPref.getInt(MES_DPAD_2_BUTTON_DOWN_OFF, 12);
        this.mes_dpad_2_button_left_on = (short) sharedPref.getInt(MES_DPAD_2_BUTTON_LEFT_ON, 13);
        this.mes_dpad_2_button_left_off = (short) sharedPref.getInt(MES_DPAD_2_BUTTON_LEFT_OFF, 14);
        this.mes_dpad_2_button_right_on = (short) sharedPref.getInt(MES_DPAD_2_BUTTON_RIGHT_ON, 15);
        this.mes_dpad_2_button_right_off = (short) sharedPref.getInt(MES_DPAD_2_BUTTON_RIGHT_OFF, 16);
    }

    public boolean isFilter_unpaired_devices() {
        return this.filter_unpaired_devices;
    }

    public void setFilter_unpaired_devices(boolean filter_unpaired_devices2) {
        this.filter_unpaired_devices = filter_unpaired_devices2;
    }
}

package com.example.makekit.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;

import com.example.makekit.R;
import com.example.makekit.sensors.Accelerometer;
import com.google.android.material.button.MaterialButton;


public class FragmentGamePadHoverBit extends Fragment {
    public short START_PRESSED = 1;
    public short STOP_PRESSED = 3;
    public short TURN_LEFT_PRESSED = 5;
    public short TURN_LEFT_RELEASED = 6;
    public short TURN_RIGHT_PRESSED = 7;
    public short TURN_RIGHT_RELEASED = 8;
    public short THROTTLE20_PRESSED = 9;
    public short THROTTLE30_PRESSED = 11;
    public short THROTTLE40_PRESSED = 13;
    public short THROTTLE50_PRESSED = 15;
    public short MES_DEVICE_GESTURE_DEVICE_SHAKEN = 4;
    public short CONTROLLER = 1104;


    GamePadListener activityCommander;
    ImageButton btn_rollLeft;
    ImageButton btn_rollRight;
    ImageButton btn_throttleUp;
    ImageButton btn_throttleDown;
    ImageButton btn_settings;
    MaterialButton btn_segment_hoverbit;
    MaterialButton btn_segment_airbit;
    Button btn_start;
    Button btn_stop;
    TextView tv_throttle, tv_throttle_up, tv_throttle_down, tv_turn_left, tv_turn_right;
    Context context;
    Resources resources;
    String language;
    String trim;

    int throttle;
    private Accelerometer accelerometer;
    int gyroPos = 0;
    boolean accelerometerEnabled, expertMode = false, deviceLanguage;

    View view;

    public interface GamePadListener {
        void passDpadPress(short s, short s2);
    }

    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            activityCommander = (GamePadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game_pad_hover_bit, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        //Initializing all buttons
        btn_throttleUp = view.findViewById(R.id.btn_throttle_up_hover);
        btn_throttleDown = view.findViewById(R.id.btn_throttle_down_hover);
        btn_rollLeft = view.findViewById(R.id.btn_roll_left_hover);
        btn_rollRight = view.findViewById(R.id.btn_roll_right_hover);
        btn_start = view.findViewById(R.id.btn_start_hover);
        btn_stop = view.findViewById(R.id.btn_stop_hover);
        btn_segment_hoverbit = view.findViewById(R.id.segment_hoverbit_hover);
        btn_segment_airbit = view.findViewById(R.id.segment_airbit_hover);
        btn_settings = view.findViewById(R.id.btn_settings_hover);

        tv_throttle = view.findViewById(R.id.tv_throttle_hover);
        tv_throttle_up = view.findViewById(R.id.tv_throttle_up_hover);
        tv_throttle_down = view.findViewById(R.id.tv_throttle_down_hover);
        tv_turn_left = view.findViewById(R.id.tv_turn_left_hover);
        tv_turn_right = view.findViewById(R.id.tv_turn_right_hover);

        btn_throttleUp.setEnabled(false);
        btn_throttleDown.setEnabled(false);
        btn_stop.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            accelerometerEnabled = bundle.getBoolean("accelerometerEnabled");
            expertMode = bundle.getBoolean("expert");
            deviceLanguage = bundle.getBoolean("language");
            trim = bundle.getString("trim");
        }

        accelerometer = new Accelerometer(requireActivity());

        if (expertMode) {
            tv_turn_left.setVisibility(View.INVISIBLE);
            tv_turn_right.setVisibility(View.INVISIBLE);
            tv_throttle_up.setVisibility(View.INVISIBLE);
            tv_throttle_down.setVisibility(View.INVISIBLE);
        } else {
            tv_turn_left.setVisibility(View.VISIBLE);
            tv_turn_right.setVisibility(View.VISIBLE);
            tv_throttle_up.setVisibility(View.VISIBLE);
            tv_throttle_down.setVisibility(View.VISIBLE);
        }

        if (!deviceLanguage) {
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
            tv_turn_left.setText(resources.getString(R.string.TurnLeft));
            tv_turn_right.setText(resources.getString(R.string.TurnRight));
            tv_throttle_down.setText(resources.getString(R.string.ThrottleDown));
            tv_throttle_up.setText(resources.getString(R.string.ThrottleUp));
            language = String.valueOf(ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).toLanguageTags());
            trim = language.substring(0,2);

        } else {
            context = LocaleHelper.setLocale(getActivity(), trim);
            System.out.println("true");
            resources = context.getResources();
            tv_turn_left.setText(resources.getString(R.string.TurnLeft));
            tv_turn_right.setText(resources.getString(R.string.TurnRight));
            tv_throttle_up.setText(resources.getString(R.string.ThrottleUp));
            tv_throttle_down.setText(resources.getString(R.string.ThrottleDown));
        }

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });

        btn_segment_airbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentGamePadAirBit fragmentGamePadAirBit = new FragmentGamePadAirBit();
                Bundle bundle = new Bundle();
                bundle.putBoolean("gyroEnabled", accelerometerEnabled);
                bundle.putBoolean("expert", expertMode);
                bundle.putBoolean("language", deviceLanguage);
                bundle.putString("trim", trim);
                fragmentGamePadAirBit.setArguments(bundle);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_area, fragmentGamePadAirBit)
                        .commit();
            }
        });


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short value = START_PRESSED;
                short id = CONTROLLER;
                activityCommander.passDpadPress(id, value);
                btn_throttleUp.setEnabled(true);
                btn_throttleDown.setEnabled(true);
                throttle = 0;
                btn_stop.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.GONE);
                if (accelerometerEnabled) {
                    enableAccelerometer();
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short value = STOP_PRESSED;
                short id = CONTROLLER;
                activityCommander.passDpadPress(id, value);
                btn_throttleUp.setEnabled(false);
                btn_throttleDown.setEnabled(false);
                throttle = 0;
                tv_throttle.setText("0");
                btn_stop.setVisibility(View.GONE);
                btn_start.setVisibility(View.VISIBLE);
                disableGyroscope();
            }
        });


        btn_throttleUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (throttle < 4) {
                    throttle += 1;
                }
                switch (throttle) {
                    case 1:
                        short value = THROTTLE20_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        tv_throttle.setText("1");
                        break;
                    case 2:
                        short value2 = THROTTLE30_PRESSED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        tv_throttle.setText("2");
                        break;
                    case 3:
                        short value3 = THROTTLE40_PRESSED;
                        short id3 = CONTROLLER;
                        activityCommander.passDpadPress(id3, value3);
                        tv_throttle.setText("3");
                        break;
                    case 4:
                        short value4 = THROTTLE50_PRESSED;
                        short id4 = CONTROLLER;
                        activityCommander.passDpadPress(id4, value4);
                        tv_throttle.setText("4");
                        break;
                }

            }
        });

        btn_throttleDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                throttle -= 1;
                switch (throttle) {

                    case -1:
                        btn_start.setVisibility(View.VISIBLE);
                        short value0 = STOP_PRESSED;
                        short id0 = CONTROLLER;
                        activityCommander.passDpadPress(id0, value0);
                        btn_throttleUp.setEnabled(false);
                        btn_throttleDown.setEnabled(false);
                        btn_stop.setVisibility(View.GONE);
                        tv_throttle.setText("0");
                        break;

                    case 0:
                        short value = STOP_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        btn_throttleUp.setEnabled(false);
                        btn_throttleDown.setEnabled(false);
                        btn_start.setVisibility(View.VISIBLE);
                        btn_stop.setVisibility(View.GONE);
                        tv_throttle.setText("0");
                        break;
                    case 1:
                        short value1 = THROTTLE20_PRESSED;
                        short id1 = CONTROLLER;
                        activityCommander.passDpadPress(id1, value1);
                        tv_throttle.setText("1");
                        break;
                    case 2:
                        short value2 = THROTTLE30_PRESSED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        tv_throttle.setText("2");

                        break;
                    case 3:
                        short value3 = THROTTLE40_PRESSED;
                        short id3 = CONTROLLER;
                        activityCommander.passDpadPress(id3, value3);
                        tv_throttle.setText("3");
                        break;
                    case 4:
                        short value4 = THROTTLE50_PRESSED;
                        short id4 = CONTROLLER;
                        activityCommander.passDpadPress(id4, value4);
                        tv_throttle.setText("4");
                        break;
                }
            }
        });

        btn_rollLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = TURN_LEFT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = TURN_LEFT_RELEASED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });

        btn_rollRight.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = TURN_RIGHT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = TURN_RIGHT_RELEASED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        accelerometer.register();
    }

    @Override
    public void onPause() {
        super.onPause();
        accelerometer.unregister();
    }

    public void goToSettings() {

        Bundle bundle = new Bundle();

        bundle.putBoolean("accelerometerEnabled", accelerometerEnabled);
        bundle.putBoolean("expert", expertMode);
        bundle.putBoolean("language", deviceLanguage);
        bundle.putString("trim", trim);
        FragmentSettings fragmentSettings = new FragmentSettings();
        fragmentSettings.setArguments(bundle);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_area, fragmentSettings)
                .commit();
    }

    public void enableAccelerometer() {
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if (ry > 2.5f) {
                    short value = TURN_RIGHT_PRESSED;
                    short id = CONTROLLER;
                    activityCommander.passDpadPress(id, value);
                    gyroPos = 1;

                } else if (ry < -2.5f) {
                    short value = TURN_LEFT_PRESSED;
                    short id = CONTROLLER;
                    activityCommander.passDpadPress(id, value);
                    gyroPos = -1;

                } else if (ry < 2.5f && ry > -2.5f) {

                    if (gyroPos != 0) {

                        if (gyroPos == -1) {
                            short value = TURN_LEFT_RELEASED;
                            short id = CONTROLLER;
                            activityCommander.passDpadPress(id, value);
                        } else if (gyroPos == 1) {
                            short value = TURN_RIGHT_RELEASED;
                            short id = CONTROLLER;
                            activityCommander.passDpadPress(id, value);
                        }
                        gyroPos = 0;
                    }
                }
            }
        });
        accelerometer.register();
    }

    public void disableGyroscope() {
        accelerometer.unregister();
    }
}
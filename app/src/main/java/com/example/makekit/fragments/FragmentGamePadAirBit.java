package com.example.makekit.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.makekit.R;
import com.example.makekit.ble.BleAdapterService;
import com.example.makekit.sensors.Accelerometer;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.TimeUnit;


public class FragmentGamePadAirBit extends Fragment {
    public short THROTTLE_UP_PRESSED = 1;
    public short THROTTLE_UP_RELEASED = 2;
    public short THROTTLE_DOWN_PRESSED = 3;
    public short THROTTLE_DOWN_RELEASED = 4;
    public short YAW_LEFT_PRESSED = 5;
    public short YAW_LEFT_RELEASED = 6;
    public short YAW_RIGHT_PRESSED = 7;
    public short YAW_RIGHT_RELEASED = 8;
    public short PITCH_FORWARD_PRESSED = 9;
    public short PITCH_FORWARD_RELEASED = 10;
    public short PITCH_BACKWARDS_PRESSED = 11;
    public short PITCH_BACKWARDS_RELEASED = 12;
    public short ROLL_LEFT_PRESSED = 13;
    public short ROLL_LEFT_RELEASED = 14;
    public short ROLL_RIGHT_PRESSED = 15;
    public short ROLL_RIGHT_RELEASED = 16;
    public short CONTROLLER = 1104;


    //Buttons
    GamePadListener activityCommander;
    ImageButton btn_pitchBackwards;
    ImageButton btn_pitchForward;
    ImageButton btn_rollLeft;
    ImageButton btn_rollRight;
    ImageButton btn_throttleUp;
    ImageButton btn_throttleDown;
    ImageButton btn_yawLeft;
    ImageButton btn_yawRight;
    ImageButton btn_settings;
    MaterialButton btn_segment_hoverbit;
    MaterialButton btn_segment_airbit;
    Button btn_start;
    Button btn_stop;


    //Layouts
    LinearLayout throttle_yaw;
    LinearLayout pitch_roll;

    TextView tv_throttle;
    int throttle;
    int gyroPosX = 0;
    int gyroPosY = 0;
    int gyroPosZ = 0;
    boolean accelerometerEnabled;
    boolean flippedGamepad = false;
    Accelerometer accelerometer;
    Handler handler;
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
        view = inflater.inflate(R.layout.fragment_game_pad_airbit, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        accelerometer = new Accelerometer(requireActivity());
        Bundle bundle = this.getArguments();
        handler = new Handler();

        if (bundle != null) {
            accelerometerEnabled = bundle.getBoolean("accelerometerEnabled");
            flippedGamepad = bundle.getBoolean("flipped");
        }

        //Initializing all buttons
        btn_throttleUp = view.findViewById(R.id.btn_throttle_up);
        btn_throttleDown = view.findViewById(R.id.btn_throttle_down);
        btn_yawLeft = view.findViewById(R.id.btn_yaw_left);
        btn_yawRight = view.findViewById(R.id.btn_yaw_right);
        btn_pitchForward = view.findViewById(R.id.btn_pitch_forward);
        btn_pitchBackwards = view.findViewById(R.id.btn_pitch_backwards);
        btn_rollLeft = view.findViewById(R.id.btn_roll_left);
        btn_rollRight = view.findViewById(R.id.btn_roll_right);
        btn_start = view.findViewById(R.id.btn_start);
        btn_stop = view.findViewById(R.id.btn_stop);
        btn_segment_hoverbit = view.findViewById(R.id.segment_hoverbit);
        btn_segment_airbit = view.findViewById(R.id.segment_airbit);
        btn_settings = view.findViewById(R.id.btn_settings_air);
        tv_throttle = view.findViewById(R.id.tv_throttle_air);

        throttle_yaw = view.findViewById(R.id.throttle_yaw_btns);
        pitch_roll = view.findViewById(R.id.pitch_roll_btns);

        //Disables buttons before arming
        btn_throttleUp.setEnabled(false);
        btn_throttleDown.setEnabled(false);
        btn_yawLeft.setEnabled(false);
        btn_yawRight.setEnabled(false);
        btn_stop.setVisibility(View.GONE);


        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short value = YAW_RIGHT_PRESSED;
                short id = CONTROLLER;
                activityCommander.passDpadPress(id, value);
                btn_throttleUp.setEnabled(true);
                btn_throttleDown.setEnabled(true);
                btn_yawLeft.setEnabled(true);
                btn_yawRight.setEnabled(true);
                throttle = 1;
                tv_throttle.setText(String.valueOf(throttle));
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


                btn_stop.setVisibility(View.GONE);
                btn_start.setVisibility(View.VISIBLE);
                btn_start.setEnabled(false);
                btn_start.setBackgroundResource(R.drawable.custom_btns_grey);
                disableGyroscope();

                for (int i = throttle; i > 0; i--) {
                    short value1 = THROTTLE_DOWN_PRESSED;
                    short id1 = CONTROLLER;
                    activityCommander.passDpadPress(id1, value1);
                    throttle -= 1;
                    System.out.println(throttle);

                    handler.post(new Runnable() {
                        public void run() {
                            tv_throttle.setText(String.valueOf(throttle));
                        }
                    });

                    if (throttle < 1) {
                        short value = YAW_LEFT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        btn_throttleUp.setEnabled(false);
                        btn_throttleDown.setEnabled(false);
                        btn_yawLeft.setEnabled(false);
                        btn_yawRight.setEnabled(false);
                        btn_start.setEnabled(true);
                        btn_start.setBackgroundResource(R.drawable.custom_btns_orange);
                        throttle = 0;
                        tv_throttle.setText(String.valueOf(throttle));
                    }

                    if (throttle > 10) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            TimeUnit.MILLISECONDS.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        btn_segment_hoverbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentGamePadHoverBit fragmentGamePadHoverBit = new FragmentGamePadHoverBit();
                Bundle bundle = new Bundle();
                bundle.putBoolean("gyroEnabled", accelerometerEnabled);
                fragmentGamePadHoverBit.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_area, fragmentGamePadHoverBit).commit();
            }
        });

        btn_segment_airbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_throttleUp.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        throttle += 1;
                        tv_throttle.setText(String.valueOf(throttle));
                        short value = THROTTLE_UP_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);

                        return true;
                    case 1:
                        short value2 = THROTTLE_UP_RELEASED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });

        btn_throttleDown.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = THROTTLE_DOWN_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        if (throttle < 2) {
                            btn_start.setVisibility(View.VISIBLE);
                            btn_stop.setVisibility(View.GONE);
                            throttle = 0;
                            tv_throttle.setText(String.valueOf(throttle));
                            btn_throttleUp.setEnabled(false);
                            btn_throttleDown.setEnabled(false);
                            btn_yawLeft.setEnabled(false);
                            btn_yawRight.setEnabled(false);
                            short value1 = YAW_LEFT_PRESSED;
                            short id1 = CONTROLLER;
                            activityCommander.passDpadPress(id1, value1);
                        } else {
                            throttle -= 1;
                            tv_throttle.setText(String.valueOf(throttle));
                        }

                        return true;
                    case 1:
                        short value2 = THROTTLE_DOWN_RELEASED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });

        btn_yawRight.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = YAW_RIGHT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = YAW_RIGHT_RELEASED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });

        btn_yawLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = YAW_LEFT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = YAW_LEFT_RELEASED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });

        btn_rollLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = ROLL_LEFT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = ROLL_LEFT_RELEASED;
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
                        short value = ROLL_RIGHT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = ROLL_RIGHT_RELEASED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });


        btn_pitchForward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = PITCH_FORWARD_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = PITCH_FORWARD_RELEASED;
                        short id2 = CONTROLLER;
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });

        btn_pitchBackwards.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = PITCH_BACKWARDS_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = PITCH_BACKWARDS_RELEASED;
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

    public void enableAccelerometer() {
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if (ry > 3.0f) {
                    short value = ROLL_LEFT_PRESSED;
                    short id = CONTROLLER;
                    activityCommander.passDpadPress(id, value);
                    gyroPosY = 1;

                } else if (ry < -3.0f) {
                    short value = ROLL_RIGHT_PRESSED;
                    short id = CONTROLLER;
                    activityCommander.passDpadPress(id, value);
                    gyroPosY = -1;

                } else if (ry < 3.0f && ry > -3.0f) {

                    if (gyroPosY != 0) {

                        if (gyroPosY == -1) {
                            short value = ROLL_LEFT_RELEASED;
                            short id = CONTROLLER;
                            activityCommander.passDpadPress(id, value);
                        } else if (gyroPosY == 1) {
                            short value = ROLL_RIGHT_RELEASED;
                            short id = CONTROLLER;
                            activityCommander.passDpadPress(id, value);
                        }
                        gyroPosY = 0;
                    }
                }

                if (rx > 3.0f) {
                    short value = PITCH_FORWARD_PRESSED;
                    short id = CONTROLLER;
                    activityCommander.passDpadPress(id, value);
                    gyroPosX = 1;

                } else if (rx < -3.0f) {
                    short value = PITCH_BACKWARDS_PRESSED;
                    short id = CONTROLLER;
                    activityCommander.passDpadPress(id, value);
                    gyroPosX = -1;

                } else if (rx < 3.0f && ry > -3.0f) {

                    if (gyroPosX != 0) {

                        if (gyroPosX == -1) {
                            short value = PITCH_FORWARD_RELEASED;
                            short id = CONTROLLER;
                            activityCommander.passDpadPress(id, value);
                        } else if (gyroPosX == 1) {
                            short value = PITCH_BACKWARDS_RELEASED;
                            short id = CONTROLLER;
                            activityCommander.passDpadPress(id, value);
                        }
                        gyroPosX = 0;
                    }
                }

                if (rz > 2.5f) {
                    short value = YAW_LEFT_PRESSED;
                    short id = CONTROLLER;
                    activityCommander.passDpadPress(id, value);
                    gyroPosZ = 1;

                } else if (rz < -2.5f) {
                    short value = YAW_RIGHT_PRESSED;
                    short id = CONTROLLER;
                    activityCommander.passDpadPress(id, value);
                    gyroPosZ = -1;

                } else if (rz < 2.5f && ry > -2.5f) {

                    if (gyroPosZ != 0) {

                        if (gyroPosZ == -1) {
                            short value = YAW_LEFT_RELEASED;
                            short id = CONTROLLER;
                            activityCommander.passDpadPress(id, value);
                        } else if (gyroPosZ == 1) {
                            short value = YAW_RIGHT_RELEASED;
                            short id = CONTROLLER;
                            activityCommander.passDpadPress(id, value);
                        }
                        gyroPosZ = 0;
                    }
                }
            }
        });
        accelerometer.register();
    }

    public void disableGyroscope() {
        accelerometer.unregister();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void goToSettings() {
        FragmentSettings fragmentSettings = new FragmentSettings();
        Bundle bundle1 = new Bundle();
        bundle1.putBoolean("airbit", true);
        bundle1.putBoolean("accelerometerEnabled", accelerometerEnabled);
        fragmentSettings.setArguments(bundle1);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_area, fragmentSettings)
                .commit();
    }

}

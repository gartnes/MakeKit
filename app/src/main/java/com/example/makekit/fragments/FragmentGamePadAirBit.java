package com.example.makekit.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.makekit.R;
import com.example.makekit.sensors.Gyroscope;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;


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

    GamePadListener activityCommander;
    ImageButton btn_pitchBackwards;
    ImageButton btn_pitchForward;
    ImageButton btn_rollLeft;
    ImageButton btn_rollRight;
    ImageButton btn_throttleUp;
    ImageButton btn_throttleDown;
    ImageButton btn_yawLeft;
    ImageButton btn_yawRight;
    MaterialButton btn_segment_hoverbit;
    MaterialButton btn_segment_airbit;
    Button btn_start;
    Button btn_stop;
    Slider slider;
    int throttle;
    float trim;
    private Gyroscope gyroscope;
    int gyroPos = 0;
    boolean gyroscopeEnabled = false;

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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
        slider = view.findViewById(R.id.slider_hoverbit);

        //Default layout with Hover:Bit selected

        btn_throttleUp.setEnabled(false);
        btn_throttleDown.setEnabled(false);
        btn_stop.setVisibility(View.GONE);

        gyroscope = new Gyroscope(getActivity());

        /*if (gyroscopeEnabled) {
            gyroscope.setListener(new Gyroscope.Listener() {
                @Override
                public void onRotation(float rx, float ry, float rz) {

                    if (rx > 1.5f) {
                        short value = TURN_RIGHT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        gyroPos = 1;

                    } else if (rx < -1.5f) {
                        short value = TURN_LEFT_PRESSED;
                        short id = CONTROLLER;
                        activityCommander.passDpadPress(id, value);
                        gyroPos = -1;

                    } else if (rx < 1.5f && rx > -1.5f) {

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
        }*/


        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short value = THROTTLE_UP_PRESSED;
                short id = CONTROLLER;
                activityCommander.passDpadPress(id, value);
                btn_throttleUp.setEnabled(true);
                btn_throttleDown.setEnabled(true);
                throttle = 0;
                btn_stop.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.GONE);


            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short value = THROTTLE_DOWN_PRESSED;
                short id = CONTROLLER;
                activityCommander.passDpadPress(id, value);
                btn_throttleUp.setEnabled(false);
                btn_throttleDown.setEnabled(false);
                throttle = 0;
                btn_stop.setVisibility(View.GONE);
                btn_start.setVisibility(View.VISIBLE);
            }
        });

        btn_segment_hoverbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_yawLeft.setVisibility(View.INVISIBLE);
                btn_yawRight.setVisibility(View.INVISIBLE);
                btn_pitchForward.setVisibility(View.INVISIBLE);
                btn_pitchBackwards.setVisibility(View.INVISIBLE);
            }
        });

        btn_segment_airbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_yawLeft.setVisibility(View.VISIBLE);
                btn_yawRight.setVisibility(View.VISIBLE);
                btn_pitchForward.setVisibility(View.VISIBLE);
                btn_pitchBackwards.setVisibility(View.VISIBLE);
            }
        });

        btn_throttleUp.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
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
                        btn_start.setVisibility(View.VISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();
        gyroscope.register();
    }

    @Override
    public void onPause() {
        super.onPause();
        gyroscope.unregister();
    }
}

package com.example.makekit.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.makekit.R;

public class FragmentGamePad extends Fragment {
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
    public short MES_DPAD_CONTROLLER = 1104;

    GamePadListener activityCommander;
    ImageButton btn_pitchBackwards;
    ImageButton btn_pitchForward;
    ImageButton btn_rollLeft;
    ImageButton btn_rollRight;
    ImageButton btn_throttleUp;
    ImageButton btn_throttleDown;
    ImageButton btn_yawLeft;
    ImageButton btn_yawRight;

    View view;

    public interface GamePadListener {
        void passDpadPress(short s, short s2);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommander = (GamePadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game_pad, container, false);
        btn_throttleUp = view.findViewById(R.id.btn_throttle_up);
        btn_throttleUp.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = THROTTLE_UP_PRESSED;
                        short id = MES_DPAD_CONTROLLER;
                        btn_throttleUp.setImageResource(R.drawable.ic_throttle_up);
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = THROTTLE_UP_RELEASED;
                        short id2 = MES_DPAD_CONTROLLER;
                        btn_throttleUp.setImageResource(R.drawable.ic_throttle_up);
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        btn_throttleDown = view.findViewById(R.id.btn_throttle_down);
        btn_throttleDown.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = THROTTLE_DOWN_PRESSED;
                        short id = MES_DPAD_CONTROLLER;
                        btn_throttleDown.setImageResource(R.drawable.ic_throttle_down);
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = THROTTLE_DOWN_RELEASED;
                        short id2 = MES_DPAD_CONTROLLER;
                        btn_throttleDown.setImageResource(R.drawable.ic_throttle_down);
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        btn_yawLeft = view.findViewById(R.id.btn_yaw_left);
        btn_yawLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = YAW_LEFT_PRESSED;
                        short id = MES_DPAD_CONTROLLER;
                        btn_yawLeft.setImageResource(R.drawable.ic_rotate_left);
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = YAW_LEFT_RELEASED;
                        short id2 = MES_DPAD_CONTROLLER;
                        btn_yawLeft.setImageResource(R.drawable.ic_rotate_left);
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        btn_yawRight = view.findViewById(R.id.btn_yaw_right);
        btn_yawRight.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = YAW_RIGHT_PRESSED;
                        short id = MES_DPAD_CONTROLLER;
                        btn_yawRight.setImageResource(R.drawable.ic_rotate_right);
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = YAW_RIGHT_RELEASED;
                        short id2 = MES_DPAD_CONTROLLER;
                        btn_yawRight.setImageResource(R.drawable.ic_rotate_right);
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        btn_pitchForward = view.findViewById(R.id.btn_pitch_forward);
        btn_pitchForward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = PITCH_FORWARD_PRESSED;
                        short id = MES_DPAD_CONTROLLER;
                        btn_pitchForward.setImageResource(R.drawable.ic_arrow_up);
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = PITCH_FORWARD_RELEASED;
                        short id2 = MES_DPAD_CONTROLLER;
                        btn_pitchForward.setImageResource(R.drawable.ic_arrow_up);
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        btn_pitchBackwards = view.findViewById(R.id.btn_pitch_backwards);
        btn_pitchBackwards.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = PITCH_BACKWARDS_PRESSED;
                        short id = MES_DPAD_CONTROLLER;
                        btn_pitchBackwards.setImageResource(R.drawable.ic_arrow_down);
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = PITCH_BACKWARDS_RELEASED;
                        short id2 = MES_DPAD_CONTROLLER;
                        btn_pitchBackwards.setImageResource(R.drawable.ic_arrow_down);
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        btn_rollLeft = view.findViewById(R.id.btn_roll_left);
        btn_rollLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = ROLL_LEFT_PRESSED;
                        short id = MES_DPAD_CONTROLLER;
                        btn_rollLeft.setImageResource(R.drawable.ic_arrow_left);
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = ROLL_LEFT_RELEASED;
                        short id2 = MES_DPAD_CONTROLLER;
                        btn_rollLeft.setImageResource(R.drawable.ic_arrow_left);
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        btn_rollRight = view.findViewById(R.id.btn_roll_right);
        btn_rollRight.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = ROLL_RIGHT_PRESSED;
                        short id = MES_DPAD_CONTROLLER;
                        btn_rollRight.setImageResource(R.drawable.ic_arrow_right);
                        activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = ROLL_RIGHT_RELEASED;
                        short id2 = MES_DPAD_CONTROLLER;
                        btn_rollRight.setImageResource(R.drawable.ic_arrow_right);
                        activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        return view;
    }
}

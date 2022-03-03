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
    public short CIRCLEPAD_DOWN_PRESSED = 11;
    public short CIRCLEPAD_DOWN_RELEASED = 12;
    public short CIRCLEPAD_LEFT_PRESSED = 13;
    public short CIRCLEPAD_LEFT_RELEASED = 14;
    public short CIRCLEPAD_RIGHT_PRESSED = 15;
    public short CIRCLEPAD_RIGHT_RELEASED = 16;
    public short CIRCLEPAD_UP_PRESSED = 9;
    public short CIRCLEPAD_UP_RELEASED = 10;
    public short DPAD_DOWN_PRESSED = 3;
    public short DPAD_DOWN_RELEASED = 4;
    public short DPAD_LEFT_PRESSED = 5;
    public short DPAD_LEFT_RELEASED = 6;
    public short DPAD_RIGHT_PRESSED = 7;
    public short DPAD_RIGHT_RELEASED = 8;
    public short DPAD_UP_PRESSED = 1;
    public short DPAD_UP_RELEASED = 2;
    public short MES_DPAD_CONTROLLER = 1104;
    GamePadListener activityCommander;
    ImageButton downCircleButton;
    ImageButton downDpadButton;
    ImageButton leftCircleButton;
    ImageButton leftDpadButton;
    ImageButton rightCircleButton;
    ImageButton rightDpadButton;
    ImageButton upCircleButton;
    ImageButton upDpadButton;
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
            this.activityCommander = (GamePadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_game_pad, container, false);
        this.upDpadButton = (ImageButton) this.view.findViewById(R.id.btn_throttle_up);
        this.upDpadButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = FragmentGamePad.this.DPAD_UP_PRESSED;
                        short id = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.upDpadButton.setImageResource(R.drawable.ic_throttle_up);
                        FragmentGamePad.this.activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = FragmentGamePad.this.DPAD_UP_RELEASED;
                        short id2 = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.upDpadButton.setImageResource(R.drawable.ic_throttle_up);
                        FragmentGamePad.this.activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.downDpadButton = (ImageButton) this.view.findViewById(R.id.btn_throttle_down);
        this.downDpadButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = FragmentGamePad.this.DPAD_DOWN_PRESSED;
                        short id = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.downDpadButton.setImageResource(R.drawable.ic_throttle_down);
                        FragmentGamePad.this.activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = FragmentGamePad.this.DPAD_DOWN_RELEASED;
                        short id2 = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.downDpadButton.setImageResource(R.drawable.ic_throttle_down);
                        FragmentGamePad.this.activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.leftDpadButton = (ImageButton) this.view.findViewById(R.id.btn_yaw_left);
        this.leftDpadButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = FragmentGamePad.this.DPAD_LEFT_PRESSED;
                        short id = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.leftDpadButton.setImageResource(R.drawable.ic_rotate_left);
                        FragmentGamePad.this.activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = FragmentGamePad.this.DPAD_LEFT_RELEASED;
                        short id2 = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.leftDpadButton.setImageResource(R.drawable.ic_rotate_left);
                        FragmentGamePad.this.activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.rightDpadButton = (ImageButton) this.view.findViewById(R.id.btn_yaw_right);
        this.rightDpadButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = FragmentGamePad.this.DPAD_RIGHT_PRESSED;
                        short id = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.rightDpadButton.setImageResource(R.drawable.ic_rotate_right);
                        FragmentGamePad.this.activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = FragmentGamePad.this.DPAD_RIGHT_RELEASED;
                        short id2 = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.rightDpadButton.setImageResource(R.drawable.ic_rotate_right);
                        FragmentGamePad.this.activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.upCircleButton = (ImageButton) this.view.findViewById(R.id.btn_pitch_forward);
        this.upCircleButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = FragmentGamePad.this.CIRCLEPAD_UP_PRESSED;
                        short id = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.upCircleButton.setImageResource(R.drawable.ic_arrow_up);
                        FragmentGamePad.this.activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = FragmentGamePad.this.CIRCLEPAD_UP_RELEASED;
                        short id2 = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.upCircleButton.setImageResource(R.drawable.ic_arrow_up);
                        FragmentGamePad.this.activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.downCircleButton = (ImageButton) this.view.findViewById(R.id.btn_pitch_backwards);
        this.downCircleButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = FragmentGamePad.this.CIRCLEPAD_DOWN_PRESSED;
                        short id = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.downCircleButton.setImageResource(R.drawable.ic_arrow_down);
                        FragmentGamePad.this.activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = FragmentGamePad.this.CIRCLEPAD_DOWN_RELEASED;
                        short id2 = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.downCircleButton.setImageResource(R.drawable.ic_arrow_down);
                        FragmentGamePad.this.activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.leftCircleButton = (ImageButton) this.view.findViewById(R.id.btn_roll_left);
        this.leftCircleButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = FragmentGamePad.this.CIRCLEPAD_LEFT_PRESSED;
                        short id = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.leftCircleButton.setImageResource(R.drawable.ic_arrow_left);
                        FragmentGamePad.this.activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = FragmentGamePad.this.CIRCLEPAD_LEFT_RELEASED;
                        short id2 = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.leftCircleButton.setImageResource(R.drawable.ic_arrow_left);
                        FragmentGamePad.this.activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.rightCircleButton = (ImageButton) this.view.findViewById(R.id.btn_roll_right);
        this.rightCircleButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        short value = FragmentGamePad.this.CIRCLEPAD_RIGHT_PRESSED;
                        short id = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.rightCircleButton.setImageResource(R.drawable.ic_arrow_right);
                        FragmentGamePad.this.activityCommander.passDpadPress(id, value);
                        return true;
                    case 1:
                        short value2 = FragmentGamePad.this.CIRCLEPAD_RIGHT_RELEASED;
                        short id2 = FragmentGamePad.this.MES_DPAD_CONTROLLER;
                        FragmentGamePad.this.rightCircleButton.setImageResource(R.drawable.ic_arrow_right);
                        FragmentGamePad.this.activityCommander.passDpadPress(id2, value2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        return this.view;
    }
}

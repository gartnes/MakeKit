package com.example.makekit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

public class MainScreen extends AppCompatActivity {

    ImageButton btn_yaw_left, btn_yaw_right,
            btn_pitch_forward, btn_pitch_backwards;
    Button btn_throttle_up, btn_throttle_down, btn_start, segment_hoverbit, segment_airbit;

    int throttle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Buttons
        segment_hoverbit = findViewById(R.id.segment_hoverbit);
        segment_airbit = findViewById(R.id.segment_airbit);
        btn_start = findViewById(R.id.btn_start);
        btn_throttle_up = findViewById(R.id.btn_throttle_up);
        btn_throttle_down = findViewById(R.id.btn_throttle_down);
        btn_pitch_forward = findViewById(R.id.btn_pitch_forward);
        btn_pitch_backwards = findViewById(R.id.btn_pitch_backwards);

        //Layouts
        LinearLayout buttons_yaw = findViewById(R.id.linlay_yaw);

        segment_hoverbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons_yaw.setVisibility(View.INVISIBLE);
                btn_pitch_forward.setVisibility(View.INVISIBLE);
                btn_pitch_backwards.setVisibility(View.INVISIBLE);
            }
        });

        segment_airbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons_yaw.setVisibility(View.VISIBLE);
                btn_pitch_forward.setVisibility(View.VISIBLE);
                btn_pitch_backwards.setVisibility(View.VISIBLE);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_start.setVisibility(View.INVISIBLE);
                throttle = 10;
            }
        });

        btn_throttle_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throttle += 1;
            }
        });
        btn_throttle_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (throttle > 0){
                    throttle -= 1;
                }
                else {
                    btn_start.setVisibility(View.VISIBLE);
                }

            }
        });

    }
}
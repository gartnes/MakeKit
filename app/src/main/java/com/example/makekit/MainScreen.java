package com.example.makekit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

public class MainScreen extends AppCompatActivity {

    Button segment_hoverbit, segment_airbit, btn_start,
            throttle_up, throttle_down, yaw_left, yaw_right;

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
        throttle_up = findViewById(R.id.btn_throttle_up);
        throttle_down = findViewById(R.id.btn_throttle_down);

        LinearLayout buttons_yaw = findViewById(R.id.linlay_yaw);

        segment_hoverbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons_yaw.setVisibility(View.INVISIBLE);
            }
        });

        segment_airbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons_yaw.setVisibility(View.VISIBLE);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_start.setVisibility(View.INVISIBLE);
                throttle = 10;
            }
        });

        throttle_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throttle += 1;
            }
        });
        throttle_down.setOnClickListener(new View.OnClickListener() {
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
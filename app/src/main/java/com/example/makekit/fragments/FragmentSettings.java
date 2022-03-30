package com.example.makekit.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;


import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.makekit.R;
import com.example.makekit.MainActivity;


public class FragmentSettings extends Fragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    View view;
    boolean gyroscopeEnabled = false;
    SwitchCompat gyroSwitch;
    ImageButton btn_back;
    Button btn_connect_microbit;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        gyroSwitch = view.findViewById(R.id.settings_switch_gyro);
        btn_back = view.findViewById(R.id.btn_back_settings);
        btn_connect_microbit = view.findViewById(R.id.btn_connect_settings);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            gyroscopeEnabled = bundle.getBoolean("gyroEnabled");
            gyroSwitch.setChecked(gyroscopeEnabled);

        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putBoolean("gyroEnabled", gyroscopeEnabled);

                FragmentGamePadHoverBit fragmentGamePadHoverBit = new FragmentGamePadHoverBit();
                fragmentGamePadHoverBit.setArguments(bundle);

                getActivity().getSupportFragmentManager().
                        beginTransaction()
                        .replace(R.id.fragment_area, fragmentGamePadHoverBit)
                        .commit();
            }
        });

        btn_connect_microbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewActivity();
            }
        });

        gyroSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gyroscopeEnabled = compoundButton.isChecked();
            }
        });

        return view;

    }

    private void moveToNewActivity () {

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        ((MainActivity) getActivity()).overridePendingTransition(0, 0);

    }
}

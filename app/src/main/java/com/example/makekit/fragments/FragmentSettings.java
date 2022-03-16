package com.example.makekit.fragments;

import android.net.Uri;
import android.os.Bundle;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import com.example.makekit.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class FragmentSettings extends Fragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    View view;
    Boolean gyroscopeEnabled;
    SwitchMaterial gyroSwitch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        gyroSwitch = view.findViewById(R.id.settings_switch_gyro);

        gyroSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gyroscopeEnabled = compoundButton.isChecked();
            }
        });



        return view;

    }
}

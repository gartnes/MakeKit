package com.example.makekit.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.appcompat.widget.SwitchCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;

import com.example.makekit.R;
import com.example.makekit.MainActivity;


public class FragmentSettings extends Fragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    View view;
    boolean accelerometerEnabled, deviceLanguage = false, goBackToAirbit = false, expertMode;
    SwitchCompat accelerometerSwitch, languageSwitch, expertSwitch;
    TextView tv_gyrosteering, tv_deviceLanguage, tv_expertMode;
    ImageButton btn_back;
    Button btn_connect_microbit;
    Context context;
    Resources resources;
    String trim, language;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        accelerometerSwitch = view.findViewById(R.id.settings_switch_gyro);
        languageSwitch = view.findViewById(R.id.settings_switch_flip);
        btn_back = view.findViewById(R.id.btn_back_settings);
        btn_connect_microbit = view.findViewById(R.id.btn_connect_settings);
        expertSwitch = view.findViewById(R.id.settings_switch_expertmode);
        tv_deviceLanguage = view.findViewById(R.id.tv_language_settings);
        tv_gyrosteering = view.findViewById(R.id.tv_gyro_settings);
        tv_expertMode = view.findViewById(R.id.tv_expertmode_settings);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            accelerometerEnabled = bundle.getBoolean("accelerometerEnabled");
            accelerometerSwitch.setChecked(accelerometerEnabled);
            goBackToAirbit = bundle.getBoolean("airbit");
            expertMode = bundle.getBoolean("expert");
            expertSwitch.setChecked(expertMode);
            deviceLanguage = bundle.getBoolean("language");
            languageSwitch.setChecked(deviceLanguage);
            trim = bundle.getString("trim");
        }

        if (!deviceLanguage) {
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
            language = String.valueOf(ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).toLanguageTags());
            trim = language.substring(0,2);
            tv_expertMode.setText(resources.getString(R.string.expert_mode));
            tv_deviceLanguage.setText(resources.getString(R.string.device_language));
            tv_gyrosteering.setText(resources.getString(R.string.gyroscope_steering));
        } else {
            context = LocaleHelper.setLocale(getActivity(), trim);
            System.out.println("true");
            resources = context.getResources();
            tv_expertMode.setText(resources.getString(R.string.expert_mode));
            tv_deviceLanguage.setText(resources.getString(R.string.device_language));
            tv_gyrosteering.setText(resources.getString(R.string.gyroscope_steering));
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!goBackToAirbit){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("accelerometerEnabled", accelerometerEnabled);
                    bundle.putBoolean("language", deviceLanguage);
                    bundle.putBoolean("expert", expertMode);
                    bundle.putString("trim", trim);

                    FragmentGamePadHoverBit fragmentGamePadHoverBit = new FragmentGamePadHoverBit();
                    fragmentGamePadHoverBit.setArguments(bundle);

                    getActivity().getSupportFragmentManager().
                            beginTransaction()
                            .replace(R.id.fragment_area, fragmentGamePadHoverBit)
                            .commit();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("accelerometerEnabled", accelerometerEnabled);
                    bundle.putBoolean("language", deviceLanguage);
                    bundle.putBoolean("expert", expertMode);
                    bundle.putString("trim", trim);

                    FragmentGamePadAirBit fragmentGamePadAirBit = new FragmentGamePadAirBit();
                    fragmentGamePadAirBit.setArguments(bundle);

                    getActivity().getSupportFragmentManager().
                            beginTransaction()
                            .replace(R.id.fragment_area, fragmentGamePadAirBit)
                            .commit();
                }
            }
        });

        btn_connect_microbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewActivity();
            }
        });

        accelerometerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                accelerometerEnabled = compoundButton.isChecked();
            }
        });

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                deviceLanguage = compoundButton.isChecked();

                Bundle bundle = new Bundle();
                bundle.putBoolean("accelerometerEnabled", accelerometerEnabled);
                bundle.putBoolean("language", deviceLanguage);
                bundle.putBoolean("expert", expertMode);
                bundle.putString("trim", trim);

                FragmentSettings fragmentSettings = new FragmentSettings();
                fragmentSettings.setArguments(bundle);

                getActivity().getSupportFragmentManager().
                        beginTransaction()
                        .replace(R.id.fragment_area, fragmentSettings)
                        .commit();
            }
        });

        expertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                expertMode = compoundButton.isChecked();
            }
        });

        return view;

    }

    private void moveToNewActivity () {

        Intent i = new Intent(getActivity(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("language", deviceLanguage);
        bundle.putString("trim", trim);
        bundle.putBoolean("expert", expertMode);
        i.putExtras(bundle);
        startActivity(i);
        ((MainActivity) getActivity()).overridePendingTransition(0, 0);

    }
}

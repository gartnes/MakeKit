package com.example.makekit;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentHelp extends Fragment {
    TextView codingLinkGuide;
    TextView helpGuideText;
    private OnFragmentInteractionListener mListener;
    TextView pairingLinkGuide;
    View view;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(C0596R.layout.fragment_help, container, false);
        this.helpGuideText = (TextView) this.view.findViewById(C0596R.C0598id.help_textView);
        this.helpGuideText.setMovementMethod(new ScrollingMovementMethod());
        this.pairingLinkGuide = (TextView) this.view.findViewById(C0596R.C0598id.PairingLink);
        this.codingLinkGuide = (TextView) this.view.findViewById(C0596R.C0598id.CodingLink);
        this.pairingLinkGuide.setMovementMethod(LinkMovementMethod.getInstance());
        this.codingLinkGuide.setMovementMethod(LinkMovementMethod.getInstance());
        return this.view;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}

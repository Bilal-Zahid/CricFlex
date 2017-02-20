package com.example.cricflex;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FragmentHelp extends Fragment {

    public FragmentHelp(){}

    TextView charge_band;
    TextView position_band;
    TextView connect_band;
    TextView calibration;
    TextView definition;
    TextView sync_data;
    TextView save_session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        charge_band = (TextView) rootView.findViewById(R.id.charge_band);
        position_band = (TextView) rootView.findViewById(R.id.position_band);
        connect_band = (TextView) rootView.findViewById(R.id.connect_band);
        calibration = (TextView) rootView.findViewById(R.id.calibration);
        definition = (TextView) rootView.findViewById(R.id.definition);
        sync_data = (TextView) rootView.findViewById(R.id.sync_data);
        save_session = (TextView) rootView.findViewById(R.id.save_session);


        charge_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(getActivity(), 1);
            }
        });
        position_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(getActivity(), 2);
            }
        });
        connect_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(getActivity(), 3);
            }
        });
        calibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(getActivity(), 4);
            }
        });
        definition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(getActivity(), 5);
            }
        });
        sync_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(getActivity(), 6);
            }
        });
        save_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(getActivity(), 7);
            }
        });



        return rootView;
    }


//    http://stackoverflow.com/questions/37038835/how-do-i-create-a-popup-overlay-view-in-an-activity-without-fragment

    private void showMyDialog(Context context, int i) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);


        switch(i) {
            case 1:
                dialog.setContentView(R.layout.help_popup_1);
                break;
            case 2:
                dialog.setContentView(R.layout.help_popup_2);
                break;
            case 3:
                dialog.setContentView(R.layout.help_popup_3);
                break;
            case 4:
                dialog.setContentView(R.layout.help_popup_4);
                break;
            case 5:
                dialog.setContentView(R.layout.help_popup_5);
                break;
            case 6:
                dialog.setContentView(R.layout.help_popup_6);
                break;
            case 7:
                dialog.setContentView(R.layout.help_popup_7);
                break;
            default:
                break;
        }



        /**
         * if you want the dialog to be specific size, do the following
         * this will cover 85% of the screen (85% width and 85% height)
         */
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
    }

}

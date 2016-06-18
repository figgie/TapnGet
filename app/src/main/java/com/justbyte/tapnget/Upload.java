package com.justbyte.tapnget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Upload extends Fragment {

    View view;
    Button b;
    FloatingActionButton fabDevice, fabScan;
    private boolean FAB_Status = false;

    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;

    public Upload(){

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            FAB_Status = false;
            if (!isVisibleToUser) {
                hideFAB();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        b = (Button)view.findViewById(R.id.b);
        fabDevice = (FloatingActionButton)view.findViewById(R.id.upload_fab_device);
        fabScan = (FloatingActionButton)view.findViewById(R.id.upload_fab_scan);

        show_fab_1 = AnimationUtils.loadAnimation(getContext(), R.anim.b1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getContext(), R.anim.b1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getContext(), R.anim.b2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getContext(), R.anim.b2_hide);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!FAB_Status) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        fabDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Document"),1);
            }
        });

        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Scan", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = null;
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                Log.w("IV", uri.getPath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void expandFAB() {

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fabDevice.getLayoutParams();
        layoutParams1.bottomMargin += (int) (fabDevice.getWidth() * 1.5);
        fabDevice.setLayoutParams(layoutParams1);
        fabDevice.startAnimation(show_fab_1);
        fabDevice.setClickable(true);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fabScan.getLayoutParams();
        layoutParams2.topMargin += (int) (fabScan.getWidth() * 1.5);
        fabScan.setLayoutParams(layoutParams2);
        fabScan.startAnimation(show_fab_2);
        fabScan.setClickable(true);

    }

    private void hideFAB() {

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fabDevice.getLayoutParams();
        layoutParams1.bottomMargin -= (int) (fabDevice.getWidth() * 1.5);
        fabDevice.setLayoutParams(layoutParams1);
        fabDevice.startAnimation(hide_fab_1);
        fabDevice.setClickable(false);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fabScan.getLayoutParams();
        layoutParams2.topMargin -= (int) (fabScan.getWidth() * 1.5);
        fabScan.setLayoutParams(layoutParams2);
        fabScan.startAnimation(hide_fab_2);
        fabScan.setClickable(false);


    }
}

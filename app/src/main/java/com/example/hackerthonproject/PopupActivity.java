package com.example.hackerthonproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class PopupActivity extends Activity implements View.OnClickListener {

    Button level, Description_Level;
    ImageButton to_mainPage;
    TextView message, point, wallet, breakdown, profile, image, setting, settings, logOut;
    TextView userPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);


        // 등급와 등급별 혜택 버튼
        level = findViewById(R.id.level);
        Description_Level = findViewById(R.id.Description_Level);

        // id 매핑
        message = findViewById(R.id.message);
        point = findViewById(R.id.point);
        wallet = findViewById(R.id.wallet);
        breakdown = findViewById(R.id.breakdown);
        profile = findViewById(R.id.profile);
        image = findViewById(R.id.image);
        setting = findViewById(R.id.setting);
        settings = findViewById(R.id.settings);
        logOut = findViewById(R.id.logOut);
        userPoint = findViewById(R.id.userPoint);
        to_mainPage = findViewById(R.id.to_mainPage);

        to_mainPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PopupActivity.this, MapsNaverActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {

    }

//    public String info() {
//        return "제발 되라";
//    }



//    private void popupWindow() {
//        try {
//            Context mContext = getApplicationContext();
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//            View layout = inflater.inflate(R.layout.activity_mypage,
//                    (ViewGroup) findViewById(R.id.popup));
//            PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.activity_mypage,
//                    null, false), 480, 800, true);
//
//            point = (TextView) layout
//                    .findViewById(R.id.point);
//            point.setText("Recording"); //Update the TextView
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}


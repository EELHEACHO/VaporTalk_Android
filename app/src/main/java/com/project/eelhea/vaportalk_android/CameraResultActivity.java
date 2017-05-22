package com.project.eelhea.vaportalk_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.eelhea.vaportalk_android.utils.DialogTimer;

/**
 * Created by eelhea on 2017. 4. 10..
 */

public class CameraResultActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{

    private static final String TAG = "CameraResultActivity";
    String receiverEmail;
    String imagePath;
    ImageView camera_result_image;

    //camera result menu
    FloatingActionButton timer_floating_btn;
    DialogTimer dialogTimer;
    TextView tv_setTime;
    int minute;
    int second;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.setContentView(R.layout.activity_camera_result);

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout)layoutInflater.inflate(R.layout.activity_camera_result_menu, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        win.addContentView(linearLayout, params);

        camera_result_image = (ImageView)findViewById(R.id.camera_result_image);

        //Extra에 picturePath라는 이미지 경로가 있으면 받아와서 camera_result_image ImageView에 넣는다
        Intent fromStartIntent = getIntent();
        if(fromStartIntent.hasExtra("picturePath")){
            receiverEmail = fromStartIntent.getExtras().getString("receiverEmail");
            imagePath = fromStartIntent.getExtras().getString("picturePath");
            Glide.with(getApplicationContext()).load(imagePath).error(R.mipmap.ic_launcher).into(camera_result_image);
        }

        //camera_result_menu
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraStartActivity.class);
                finish();
                startActivity(intent);
            }
        });

        tv_setTime = (TextView)findViewById(R.id.tv_setTime);
        timer_floating_btn = (FloatingActionButton)findViewById(R.id.timer_floating_btn);
        timer_floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTimer = new DialogTimer(CameraResultActivity.this, ok_btn_click_listener);
                dialogTimer.show();
            }

            Button.OnClickListener ok_btn_click_listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDismiss(dialogTimer);
                    tv_setTime.setText(minute+ " 분 " + second + " 초");
                }
            };
        });


        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        DialogTimer dialogTimer = (DialogTimer) dialog;
        minute = dialogTimer.getMinute();
        second = dialogTimer.getSecond();
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), CameraStartActivity.class);
        finish();
        startActivity(intent);
    }


}

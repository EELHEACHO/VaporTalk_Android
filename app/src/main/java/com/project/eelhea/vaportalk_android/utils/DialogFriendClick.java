package com.project.eelhea.vaportalk_android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.eelhea.vaportalk_android.R;

/**
 * Created by eelhea on 2017. 5. 2..
 */

public class DialogFriendClick extends Dialog{

    private Activity activity;
    private ImageView dialog_close;
    private View.OnClickListener close_click_listener;
    private ImageView dialog_profile;
    private String profile;
    private TextView dialog_name;
    private String name;
    private TextView dialog_email;
    private String email;
    private Button dialog_send_btn;
    private View.OnClickListener send_click_listener;

    public DialogFriendClick (Activity activity, String profile, String name, String email, View.OnClickListener close_click_listener, View.OnClickListener send_click_listener){
        super(activity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        this.activity = activity;
        this.profile = profile;
        this.name = name;
        this.email = email;
        this.close_click_listener = close_click_listener;
        this.send_click_listener = send_click_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_friend_click);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setDimAmount(0.6f);

        dialog_close = (ImageView)findViewById(R.id.dialog_close);
        dialog_close.setOnClickListener(close_click_listener);

        dialog_profile = (ImageView)findViewById(R.id.dialog_profile);
        Glide.with(getContext()).load(profile).placeholder(R.mipmap.ic_default_profile).into(dialog_profile);

        dialog_name= (TextView)findViewById(R.id.dialog_name);
        dialog_name.setText(name);

        dialog_email = (TextView)findViewById(R.id.dialog_email);
        dialog_email.setText(email);

        dialog_send_btn = (Button)findViewById(R.id.dialog_send_btn);
        dialog_send_btn.setOnClickListener(send_click_listener);
    }


}

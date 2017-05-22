package com.project.eelhea.vaportalk_android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.project.eelhea.vaportalk_android.R;

/**
 * Created by eelhea on 2017. 5. 7..
 */

public class DialogTimer extends Dialog{

    final static String TAG = "DialogTimer";

    private NumberPicker np_minute;
    private NumberPicker np_second;
    private Button ok_btn;
    private View.OnClickListener ok_btn_click_listener;

    public DialogTimer(Activity activity, View.OnClickListener ok_btn_click_listener){
        super(activity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        this.ok_btn_click_listener = ok_btn_click_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_timepicker);

        np_minute = (NumberPicker)findViewById(R.id.np_minute);
        np_minute.setMinValue(0);
        np_minute.setMaxValue(10);
        np_minute.setWrapSelectorWheel(true);

        np_second = (NumberPicker)findViewById(R.id.np_second);
        np_second.setMinValue(0);
        np_second.setMaxValue(60);
        np_second.setWrapSelectorWheel(true);

        ok_btn = (Button)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(ok_btn_click_listener);
    }

    public int getMinute(){
        return np_minute.getValue();
    }

    public int getSecond(){
        return np_second.getValue();
    }
}

package com.project.eelhea.vaportalk_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.project.eelhea.vaportalk_android.helper.UserMyFriendDBHelper;
import com.project.eelhea.vaportalk_android.view.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eelhea on 2017. 4. 5..
 */

public class FragSettingTab extends Fragment {

    private static final String TAG = "FragSettingTab";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setting_tab, container, false);

        final UserMyFriendDBHelper userMyFriendDBHelper = new UserMyFriendDBHelper(getContext());

        view.findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("로그아웃 하시겠습니까?")
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resetSharedPreference();
                                if(userMyFriendDBHelper.deleteAllMyFriend()){
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }).show();
            }
        });

        return view;
    }

    private void resetSharedPreference(){
        SharedPreferences pref = getActivity().getSharedPreferences("vaporTalk", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}

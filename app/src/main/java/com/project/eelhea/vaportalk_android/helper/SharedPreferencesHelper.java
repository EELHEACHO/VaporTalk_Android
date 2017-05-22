package com.project.eelhea.vaportalk_android.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.eelhea.vaportalk_android.DTO.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eelhea on 2017. 5. 14..
 */

public class SharedPreferencesHelper {

    Context context;

    public SharedPreferencesHelper(Context _context){
        this.context = _context;
    }

    public String getUserEmailFromSharedPreferences(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("vaporTalk", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");
        return userEmail;
    }

    public User getUserFromSharedPreferences(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("vaporTalk", MODE_PRIVATE);
        User user = new User(sharedPreferences.getString("userUid", ""),
                sharedPreferences.getString("userEmail",""),
                sharedPreferences.getString("userName",""),
                sharedPreferences.getString("userSex",""),
                sharedPreferences.getString("userBirthday",""),
                sharedPreferences.getString("userTel",""),
                sharedPreferences.getString("userIsCommAgree",""),
                sharedPreferences.getString("userIsNearAgree",""),
                sharedPreferences.getString("userProfileImg",""));

        return user;
    }

    public void clearUserSharedPreferences(){
        SharedPreferences pref = context.getSharedPreferences("vaporTalk", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public void saveUserSharedPreferences(User loginedUser){
        SharedPreferences pref = context.getSharedPreferences("vaporTalk", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("userUid", loginedUser.getUid());
        editor.putString("userEmail", loginedUser.getEmail());
        editor.putString("userName", loginedUser.getName());
        editor.putString("userSex", loginedUser.getSex());
        editor.putString("userBirthday", loginedUser.getBirthday());
        editor.putString("userTel", loginedUser.getTel());
        editor.putString("userIsCommAgree", loginedUser.getIsCommerceAgree());
        editor.putString("userIsNearAgree", loginedUser.getIsNearAgree());
        editor.putString("userProfileImg", loginedUser.getProfileImage());
        editor.commit();
    }
}

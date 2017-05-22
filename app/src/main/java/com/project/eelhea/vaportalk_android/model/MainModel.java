package com.project.eelhea.vaportalk_android.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by eelhea on 2017. 5. 16..
 */

public class MainModel {

    public boolean checkFirebaseUser(){
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if ( mFirebaseUser == null ) {
            return false;
        } else {
            return true;
        }
    }
}

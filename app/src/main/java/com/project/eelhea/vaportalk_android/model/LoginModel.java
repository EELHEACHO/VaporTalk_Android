package com.project.eelhea.vaportalk_android.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.eelhea.vaportalk_android.helper.SharedPreferencesHelper;
import com.project.eelhea.vaportalk_android.DTO.User;
import com.project.eelhea.vaportalk_android.helper.UserMyFriendDBHelper;

/**
 * Created by eelhea on 2017. 5. 14..
 */

public class LoginModel {

    Activity activity;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    public LoginModel(Activity _activity){
        this.activity = _activity;
    }

    public int checkVaporTalkLoginValidation(String email, String pwd){
        if (TextUtils.isEmpty(email)) return 0;
        if (TextUtils.isEmpty(pwd)) return 1;
        if(pwd.length() < 6) return 2;
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd) && pwd.length() >= 6){
            return 3;
        } else {
            return 4;
        }
    }

    public void vaporTalkLogin(String email, String pwd, final FirebaseLoginListener listener){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            listener.onCompleteFail("로그인 실패");
                        } else {
                            listener.onCompleteSuccess();
                        }
                    }
                });
    }

    public interface FirebaseLoginListener {
        void onCompleteSuccess();
        void onCompleteFail(String message);
    }

    public void checkFirebaseAuthentication(final FirebaseAuthListener listener) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    listener.onAuthSuccess(user);
                }
            }
        };

        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    public interface FirebaseAuthListener {
        void onAuthSuccess(FirebaseUser user);
    }


    public boolean compareWithSharedPreferencesUserData(FirebaseUser currentUser){
        SharedPreferencesHelper prefHelper = new SharedPreferencesHelper(activity);
        String userEmail = prefHelper.getUserEmailFromSharedPreferences();

        if(userEmail.equals(currentUser.getEmail())){
            return true;
        } else {
            return false;
        }
    }

    public void clearSharedPreferencesUserData(){
        SharedPreferencesHelper prefHelper = new SharedPreferencesHelper(activity);
        prefHelper.clearUserSharedPreferences();
    }

    public void getUserFromFirebaseDatabase(FirebaseUser loginUser, final FirebaseDataListener listener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(loginUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setUid(dataSnapshot.getKey());

                listener.onDataChangeSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FBDB:onCancelled", databaseError.toException());
            }
        });
    }

    public void saveSharedPreferencesUserData(User user){
        SharedPreferencesHelper prefHelper = new SharedPreferencesHelper(activity);
        prefHelper.saveUserSharedPreferences(user);
    }

    public void getFriendListFromFirebaseDB(String uid, final FirebaseDataListener listener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("friends").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    user.setUid(userSnapshot.getKey());

                    listener.onDataChangeSuccess(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFL:onCancelled", databaseError.toException());
            }
        });
    }

    public void saveFriendListToAndroidDB(User user){
        UserMyFriendDBHelper userMyFriendDBHelper = new UserMyFriendDBHelper(activity);
        userMyFriendDBHelper.insertMyFriend(user);
    }

    public interface FirebaseDataListener {
        void onDataChangeSuccess(User user);
    }
}

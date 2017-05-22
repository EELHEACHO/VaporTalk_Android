package com.project.eelhea.vaportalk_android.model;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.eelhea.vaportalk_android.DTO.User;

import java.util.Map;

/**
 * Created by eelhea on 2017. 5. 16..
 */

public class SignupModel {

    Activity activity;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    public SignupModel(Activity _activity){
        this.activity = _activity;
    }

    public String checkSignupInputValidation(String name, String email, String pwd, String birthday, String sex, String tel){
        String message ="";
        if(TextUtils.isEmpty(name)){
            message = "이름을 입력해주세요.";
        }
        if(TextUtils.isEmpty(email)){
            message = "이메일 주소를 입력해주세요.";
        }
        if(TextUtils.isEmpty(pwd)){
            message = "비밀번호를 입력해주세요.";
        }
        if(pwd.length() < 6){
            message = "비밀번호를 6자 이상 입력해주세요.";
        }
        if(TextUtils.isEmpty(birthday)){
            message = "주민등록번호 앞 6자리를 입력해주세요.";
        }
        if(birthday.length()!=6){
            message = "주민등록번호 앞 6자리를 입력해주세요.";
        }
        if(TextUtils.isEmpty(sex)){
            message = "주민등록번호 뒤 1자리를 입력해주세요.";
        }
        if(TextUtils.isEmpty(tel)){
            message = "전화번호를 입력해주세요.";
        }

        return message;
    }

    public void createUserUsingFirebaseAuth(String email, String pwd, final FirebaseCreateUserListener listener){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        listener.onCreateComplete();
                    }
                });
    }

    public interface FirebaseCreateUserListener{
        void onCreateComplete();
    }

    public void getUserDefaultProfileImageFromFirebaseStorage(final FirebaseGetImageUriListener listener){
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://vaportalk-6725e.appspot.com/");
        storageReference.child("user.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String profileImageUri = uri.toString();
                listener.onGetSuccess(profileImageUri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onGetFailure("이미지를 찾을 수 없습니다.");
            }
        });
    }

    public interface FirebaseGetImageUriListener{
        void onGetSuccess(String profileImageUri);
        void onGetFailure(String message);
    }

    public void saveUserToFirebaseDB(String email, String name, String sex, String birthday, String tel, String isCommerceAgree, String isNearAgree, String profileImage){
        String uid = firebaseAuth.getCurrentUser().getUid();
        User user = new User(uid, email, name, sex, birthday, tel, isCommerceAgree, isNearAgree, profileImage);
        Map<String, String> userValues = user.toMap();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(uid).setValue(userValues);
    }
}

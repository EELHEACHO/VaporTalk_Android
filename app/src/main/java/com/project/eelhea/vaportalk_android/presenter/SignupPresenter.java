package com.project.eelhea.vaportalk_android.presenter;

import android.app.Activity;

import com.project.eelhea.vaportalk_android.contract.SignupContract;
import com.project.eelhea.vaportalk_android.model.SignupModel;

/**
 * Created by eelhea on 2017. 5. 16..
 */

public class SignupPresenter implements SignupContract.Presenter {

    SignupContract.View signupView;
    SignupModel signupModel;
    Activity activity;

    public SignupPresenter(SignupContract.View _signupView, Activity _activity){
        this.activity = _activity;
        this.signupView = _signupView;
        this.signupModel = new SignupModel(activity);
    }

    @Override
    public void callSignup(final String name, final String email, String pwd, final String birthday, final String sex, final String tel, final String isCommerceAgree, final String isNearAgree) {
        String validateResult = signupModel.checkSignupInputValidation(name, email, pwd, birthday, sex, tel);
        if(!validateResult.equals("")){
            signupView.showValidateMessage(validateResult);
        } else {
            signupModel.createUserUsingFirebaseAuth(email, pwd, new SignupModel.FirebaseCreateUserListener() {
                @Override
                public void onCreateComplete() {
                    signupModel.getUserDefaultProfileImageFromFirebaseStorage(new SignupModel.FirebaseGetImageUriListener() {
                        @Override
                        public void onGetSuccess(String profileImageUri) {
                            signupModel.saveUserToFirebaseDB(email, name, sex, birthday, tel, isCommerceAgree, isNearAgree, profileImageUri);
                            signupView.startLoginActivity();
                        }

                        @Override
                        public void onGetFailure(String message) {
                            signupView.showValidateMessage(message);
                        }
                    });
                }
            });
        }
    }
}

package com.project.eelhea.vaportalk_android.presenter;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.project.eelhea.vaportalk_android.contract.LoginContract;
import com.project.eelhea.vaportalk_android.DTO.User;
import com.project.eelhea.vaportalk_android.model.LoginModel;

/**
 * Created by eelhea on 2017. 5. 14..
 */

public class LoginPresenter implements LoginContract.Presenter {

    LoginContract.View loginView;
    LoginModel loginModel;
    Activity activity;

    public LoginPresenter(LoginContract.View _loginView, Activity _activity){
        this.activity = _activity;
        this.loginView = _loginView;
        this.loginModel = new LoginModel(activity);
    }

    @Override
    public void checkAutoLogin() {
        Log.e("LoginPresenter", "checkAutoLogin");
        loginModel.checkFirebaseAuthentication(new LoginModel.FirebaseAuthListener() {
            @Override
            public void onAuthSuccess(FirebaseUser user) {
                if(loginModel.compareWithSharedPreferencesUserData(user)){
                    loginView.startMainActivity();
                } else {
                    loginModel.clearSharedPreferencesUserData();
                    loginModel.getUserFromFirebaseDatabase(user, new LoginModel.FirebaseDataListener() {
                        @Override
                        public void onDataChangeSuccess(User user) {
                            loginModel.saveSharedPreferencesUserData(user);
                            loginModel.getFriendListFromFirebaseDB(user.getUid(), new LoginModel.FirebaseDataListener() {
                                @Override
                                public void onDataChangeSuccess(User user) {
                                    loginModel.saveFriendListToAndroidDB(user); // 안드로이드 내부DB에 친구목록 저장
                                    loginView.startMainActivity();
                                }
                            });
                        }
                    });
                }
                loginView.hideProgressBar();
            }
        });
    }

    @Override
    public void callLogin(String email, String pwd) {
        loginView.showProgressBar();
        int loginValidation = loginModel.checkVaporTalkLoginValidation(email, pwd);
        if(loginValidation==3){
            loginModel.vaporTalkLogin(email, pwd, new LoginModel.FirebaseLoginListener() {
                @Override
                public void onCompleteSuccess() {
                    loginView.hideProgressBar();
                }

                @Override
                public void onCompleteFail(String message) {
                    loginView.showErrorMessage(message);
                }
            });
        } else if(loginValidation==2){
            loginView.showErrorMessage("비밀번호를 6자 이상 입력해주세요");
        } else if(loginValidation==1){
            loginView.showErrorMessage("비밀번호가 틀렸습니다");
        } else if(loginValidation==0){
            loginView.showErrorMessage("존재하지 않는 이메일입니다");
        }
    }

    @Override
    public void callSignUp() {
        loginView.startSignUpActivity();
    }
}

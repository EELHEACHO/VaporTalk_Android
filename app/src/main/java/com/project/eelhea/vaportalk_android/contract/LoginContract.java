package com.project.eelhea.vaportalk_android.contract;

/**
 * Created by eelhea on 2017. 5. 14..
 */

public interface LoginContract {
    interface View {
        void showProgressBar();
        void hideProgressBar();
        void showErrorMessage(String message);
        void startMainActivity();
        void startSignUpActivity();
    }

    interface Presenter {
        void checkAutoLogin();
        void callLogin(String email, String pwd);
        void callSignUp();
    }
}

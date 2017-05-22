package com.project.eelhea.vaportalk_android.contract;

/**
 * Created by eelhea on 2017. 5. 16..
 */

public interface SignupContract {
    interface View{
        void showValidateMessage(String message);
        void startLoginActivity();
    }

    interface Presenter{
        void callSignup(String name, String email, String pwd, String birthday, String sex, String tel, String isCommerceAgree, String isNearAgree);
    }
}

package com.project.eelhea.vaportalk_android.contract;

/**
 * Created by eelhea on 2017. 5. 16..
 */

public interface MainContract {
    interface View {
        void showErrorMessage(String message);
        void startLoginActivity();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void checkFirebaseUser();
    }
}

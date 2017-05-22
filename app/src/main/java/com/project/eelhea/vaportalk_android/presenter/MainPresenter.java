package com.project.eelhea.vaportalk_android.presenter;

import com.project.eelhea.vaportalk_android.contract.MainContract;
import com.project.eelhea.vaportalk_android.model.MainModel;

/**
 * Created by eelhea on 2017. 5. 16..
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mainView;

    MainModel mainModel;

    public MainPresenter() {
        mainModel = new MainModel();
    }

    @Override
    public void attachView(MainContract.View view) {
        this.mainView = view;
    }

    @Override
    public void detachView() {
        mainView = null;
    }

    @Override
    public void checkFirebaseUser() {
        if(mainModel.checkFirebaseUser()==false) {
            mainView.showErrorMessage("로그인이 필요합니다");
            mainView.startLoginActivity();
        }
    }
}

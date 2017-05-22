package com.project.eelhea.vaportalk_android.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.eelhea.vaportalk_android.R;
import com.project.eelhea.vaportalk_android.contract.LoginContract;
import com.project.eelhea.vaportalk_android.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{

    TextView tv_title;
    EditText et_login_email;
    EditText et_login_pwd;
    ProgressBar progressBar;

    LoginContract.Presenter presenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this, LoginActivity.this);

        tv_title = (TextView)findViewById(R.id.tv_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Handlee-Regular.ttf");
        tv_title.setTypeface(typeface);

        et_login_email = (EditText)findViewById(R.id.et_login_email);
        et_login_pwd = (EditText)findViewById(R.id.et_login_pwd);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        // VaporTalk LoginContract
        findViewById(R.id.vapor_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.callLogin(et_login_email.getText().toString(), et_login_pwd.getText().toString());
            }
        });

        // VaporTalk SignUp
        findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.callSignUp();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.checkAutoLogin();
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        if ( mFirebaseAuthListener != null )
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
    }*/

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startSignUpActivity() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
        finish();
    }
}

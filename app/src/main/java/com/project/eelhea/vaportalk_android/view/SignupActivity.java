package com.project.eelhea.vaportalk_android.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.project.eelhea.vaportalk_android.R;
import com.project.eelhea.vaportalk_android.contract.SignupContract;
import com.project.eelhea.vaportalk_android.presenter.SignupPresenter;

/**
 * Created by eelhea on 2017. 4. 4..
 */

public class SignupActivity extends AppCompatActivity implements SignupContract.View{

    private EditText et_signup_name;
    private EditText et_signup_birthday;
    private EditText et_signup_sex;
    private EditText et_signup_email;
    private EditText et_signup_pwd;
    private EditText et_signup_tel;
    private CheckBox is_commerce_agree;
    private CheckBox is_near_agree;
    private Button signup_button;

    SignupContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        presenter = new SignupPresenter(this, SignupActivity.this);

        initView();
    }

    private void initView(){
        et_signup_name = (EditText)findViewById(R.id.et_signup_name);
        et_signup_birthday = (EditText)findViewById(R.id.et_signup_birthday);
        et_signup_sex = (EditText)findViewById(R.id.et_signup_sex);
        et_signup_email = (EditText)findViewById(R.id.et_signup_email);
        et_signup_pwd = (EditText)findViewById(R.id.et_signup_pwd);
        et_signup_tel = (EditText)findViewById(R.id.et_signup_tel);
        is_commerce_agree = (CheckBox)findViewById(R.id.is_commerce_agree);
        is_near_agree = (CheckBox)findViewById(R.id.is_near_agree);

        signup_button = (Button)findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_signup_name.getText().toString().trim();
                String email = et_signup_email.getText().toString().trim();
                String pwd = et_signup_pwd.getText().toString().trim();
                String birthday = et_signup_birthday.getText().toString().trim();
                String sex = et_signup_sex.getText().toString().trim();
                if(sex=="1") {
                    sex = "male";
                }
                else  sex = "female";

                String tel = et_signup_tel.getText().toString().trim();

                String isCommerceAgree;
                if(is_commerce_agree.isChecked()) {
                    isCommerceAgree = "true";
                }
                else  isCommerceAgree = "false";

                String isNearAgree;
                if(is_near_agree.isChecked()) {
                    isNearAgree = "true";
                }
                else  isNearAgree = "false";

                presenter.callSignup(name, email, pwd, birthday, sex, tel, isCommerceAgree, isNearAgree);
            }
        });
    }

    @Override
    public void showValidateMessage(String message) {
        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

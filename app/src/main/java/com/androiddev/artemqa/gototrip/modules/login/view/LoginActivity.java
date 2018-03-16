package com.androiddev.artemqa.gototrip.modules.login.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.modules.login.LoginContract;
import com.androiddev.artemqa.gototrip.modules.login.presenter.LoginPresenter;
import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.modules.main.view.MainActivity;
import com.androiddev.artemqa.gototrip.modules.register.view.RegisterActivity;
import com.androiddev.artemqa.gototrip.helper.Constants;


public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {
    private TextInputEditText mEtEmail, mEtPassword;
    private CardView mCvBtnLogin;
    private TextView mTvRegister;
    private Switch mSwRememberMe;
    private LoginPresenter mPresenter;
    private SharedPreferences mSharedPref;
    private static final String TAG = "MyLog";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initForm();
    }


    private void initForm() {

        mEtEmail = findViewById(R.id.et_email_login_a);
        mEtPassword = findViewById(R.id.et_password_login_a);
        mCvBtnLogin = findViewById(R.id.cv_btn_login_login_a);
        mCvBtnLogin.setOnClickListener(this);
        mTvRegister = findViewById(R.id.tv_register_login_a);
        mTvRegister.setOnClickListener(this);
        mSwRememberMe = findViewById(R.id.sw_remember_me_login_a);
        mSharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES_SAVE_LOGIN_NAME,Context.MODE_PRIVATE);
        mPresenter = new LoginPresenter(mSharedPref);
        mPresenter.attachView(this);
        mPresenter.viewIsReady();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_btn_login_login_a:
                mPresenter.onClickLoginButton(mEtEmail.getText().toString() , mEtPassword.getText().toString(), mSwRememberMe.isChecked());
                break;
            case R.id.tv_register_login_a:
                mPresenter.OnClickRegisterButton();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showErrorAuth(String errorMassage) {
        Toast.makeText(LoginActivity.this, R.string.toast_failed_login_a, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Registration failed with :"+ errorMassage);
    }

    @Override
    public void showSuccessAuth() {
        Toast.makeText(LoginActivity.this,R.string.toast_success_login_a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFromSharedPref(String email, String password, boolean isChecked) {
        mEtEmail.setText(email);
        mEtPassword.setText(password);
        mSwRememberMe.setChecked(isChecked);
    }

    @Override
    public void showErrorEmptyField() {
        Toast.makeText(LoginActivity.this,R.string.er_empty_fields_login_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}

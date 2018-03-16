package com.androiddev.artemqa.gototrip.modules.register.view;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.modules.login.view.LoginActivity;
import com.androiddev.artemqa.gototrip.modules.main.view.MainActivity;
import com.androiddev.artemqa.gototrip.modules.register.RegisterContract;
import com.androiddev.artemqa.gototrip.modules.register.presenter.RegisterPresenter;


public class RegisterActivity extends BaseActivity implements View.OnClickListener ,RegisterContract.View{
    private TextInputEditText mEtEmail, mEtPassword, mEtName;
    private CardView mCvBtnRegister;
    private TextView mTvLogin;
    private RegisterPresenter mPresenter;
    private static final String TAG = "MyLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initForm();
    }

    private void initForm() {
        mEtEmail = findViewById(R.id.et_email_register_a);
        mEtPassword = findViewById(R.id.et_pass_register_a);
        mEtName = findViewById(R.id.et_name_register_a);
        mTvLogin = findViewById(R.id.tv_login_register_a);
        mTvLogin.setOnClickListener(this);
        mCvBtnRegister = findViewById(R.id.cv_btn_register_register_a);
        mCvBtnRegister.setOnClickListener(this);
        mPresenter = new RegisterPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_btn_register_register_a:
                mPresenter.onButtonRegisterClicked(mEtEmail.getText().toString(),mEtName.getText().toString(), mEtPassword.getText().toString());
                break;

            case R.id.tv_login_register_a:
                mPresenter.onButtonLoginClicked();
                break;
        }

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
    public void showErrorEmptyFields() {
        Toast.makeText(RegisterActivity.this,R.string.er_empty_fields_register_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorUnvalidFields() {
        Toast.makeText(RegisterActivity.this,R.string.er_no_correct_register_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorRegister(String errorMessage) {
        Toast.makeText(RegisterActivity.this,R.string.er_no_correct_register_a,Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Registrartion failed: " + errorMessage);
    }

    @Override
    public void showSuccessRegister() {
        Toast.makeText(RegisterActivity.this,R.string.toast_success_register_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }


    public void openMainActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}

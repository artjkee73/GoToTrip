package com.androiddev.artemqa.gototrip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.helper.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity  implements View.OnClickListener{
    private TextInputEditText mEtEmail,mEtPassword;
    private CardView mCvBtnLogin;
    private TextView mTvRegister;
    private Switch mSwRememberMe;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyLog";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initForm();

    }

    private void authUser() {

        if(mSwRememberMe.isChecked()){
            saveToSharedPreferences();
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void initForm() {
        mAuth = FirebaseAuth.getInstance();

        mEtEmail = findViewById(R.id.et_email_login_a);
        mEtPassword = findViewById(R.id.et_password_login_a);
        mCvBtnLogin = findViewById(R.id.cv_btn_login_login_a);
        mCvBtnLogin.setOnClickListener(this);
        mTvRegister = findViewById(R.id.tv_register_login_a);
        mTvRegister.setOnClickListener(this);
        mSwRememberMe = findViewById(R.id.sw_remember_me_login_a);
        loadFromSharedPreferences();

    }

    private void saveToSharedPreferences() {
        SharedPreferences userSP = getSharedPreferences(Helper.SHARED_PREFERENCES_SAVE_LOGIN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSP.edit();
        editor.putString(Helper.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL,mEtEmail.getText().toString());
        editor.putString(Helper.SHARED_PREFERENCES_SAVE_LOGIN_PASSWORD, mEtPassword.getText().toString());
        editor.putBoolean(Helper.SHARED_PREFERENCES_SAVE_LOGIN_SW_IS_CHECKED,mSwRememberMe.isChecked());
        editor.apply();
    }

    private void loadFromSharedPreferences() {
        SharedPreferences userSP = getSharedPreferences(Helper.SHARED_PREFERENCES_SAVE_LOGIN_NAME,Context.MODE_PRIVATE);
        if(userSP.contains(Helper.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL)){
            mEtEmail.setText(userSP.getString(Helper.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL,""));
            mEtPassword.setText(userSP.getString(Helper.SHARED_PREFERENCES_SAVE_LOGIN_PASSWORD,""));
            mSwRememberMe.setChecked(userSP.getBoolean(Helper.SHARED_PREFERENCES_SAVE_LOGIN_SW_IS_CHECKED,false));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.cv_btn_login_login_a:
                showProgressDialog();
                loginUser(mEtEmail.getText().toString(),mEtPassword.getText().toString());
                break;
            case R.id.tv_register_login_a:
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loginUser(String email, String password) {

        if(validateForm()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgressDialog();
                                authUser();
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Вы успешно авторизованы",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                hideProgressDialog();
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Ошибка авторизации",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }else {
            Toast.makeText(LoginActivity.this, "Некорректно заполнены поля",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        boolean isValidate = false;
        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        if(email.isEmpty() && password.isEmpty() ){
            Toast.makeText(LoginActivity.this, "Поля не заполнены",
                    Toast.LENGTH_SHORT).show();
            isValidate = false;
        } else if (Pattern.matches("^.{3,}@.{3,}$",email) &&
                Pattern.matches("[A-Z_a-z_а-я_А-Я_0-9\\u002E\\u005F]{6,15}",password)){
            isValidate = true;
        }
        else isValidate = false;
        return isValidate;
    }

}

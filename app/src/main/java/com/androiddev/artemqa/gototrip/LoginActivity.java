package com.androiddev.artemqa.gototrip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.regex.Pattern;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{
    private TextInputEditText etEmail,etPassword;
    private CardView cvBtnLogin;
    private TextView tvRegister;
    private Switch swRememberMe;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyLog";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initForm();

    }

    private void authUser() {

        if(swRememberMe.isChecked()){
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
        etEmail = findViewById(R.id.et_email_login_a);
        etPassword = findViewById(R.id.et_password_login_a);
        cvBtnLogin = findViewById(R.id.cv_btn_login_login_a);
        cvBtnLogin.setOnClickListener(this);
        tvRegister = findViewById(R.id.tv_register_login_a);
        tvRegister.setOnClickListener(this);
        swRememberMe = findViewById(R.id.sw_remember_me_login_a);
        loadFromSharedPreferences();

    }

    private void saveToSharedPreferences() {
        SharedPreferences userSP = getSharedPreferences(Helper.SHARED_PREFERENCES_SAVE_LOGIN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSP.edit();
        editor.putString(Helper.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL,etEmail.getText().toString());
        editor.putString(Helper.SHARED_PREFERENCES_SAVE_LOGIN_PASSWORD, etPassword.getText().toString());
        editor.putBoolean(Helper.SHARED_PREFERENCES_SAVE_LOGIN_SW_IS_CHECKED,swRememberMe.isChecked());
        editor.apply();
    }

    private void loadFromSharedPreferences() {
        SharedPreferences userSP = getSharedPreferences(Helper.SHARED_PREFERENCES_SAVE_LOGIN_NAME,Context.MODE_PRIVATE);
        if(userSP.contains(Helper.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL)){
            etEmail.setText(userSP.getString(Helper.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL,""));
            etPassword.setText(userSP.getString(Helper.SHARED_PREFERENCES_SAVE_LOGIN_PASSWORD,""));
            swRememberMe.setChecked(userSP.getBoolean(Helper.SHARED_PREFERENCES_SAVE_LOGIN_SW_IS_CHECKED,false));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.cv_btn_login_login_a:
                loginUser(etEmail.getText().toString(),etPassword.getText().toString());
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
                                authUser();
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Вы успешно авторизованы",
                                        Toast.LENGTH_SHORT).show();
                            } else {
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
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

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

package com.androiddev.artemqa.gototrip.modules.register.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.modules.main.view.MainActivity;
import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.modules.login.view.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextInputEditText etEmail, etPassword, etName;
    private CardView cvBtnRegister;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initForm();
    }

    private void initForm() {
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.et_email_register_a);
        etPassword = findViewById(R.id.et_pass_register_a);
        etName = findViewById(R.id.et_name_register_a);
        tvLogin = findViewById(R.id.tv_login_register_a);
        tvLogin.setOnClickListener(this);
        cvBtnRegister = findViewById(R.id.cv_btn_register_register_a);
        cvBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.cv_btn_register_register_a:
                showProgressDialog();
                registerUser(etEmail.getText().toString(), etPassword.getText().toString());
                break;

            case R.id.tv_login_register_a:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void registerUser(String email, String password) {
        if (validateForm()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgressDialog();
                                startMainActivity();
                                Log.d(TAG, "createUserWithEmail:success");
                            } else {
                                hideProgressDialog();
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Ошибка входа",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else hideProgressDialog();
    }

    private void startMainActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private boolean validateForm() {
        boolean isValidate = false;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();

        if (email.isEmpty() && password.isEmpty() && name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Поля не заполнены",
                    Toast.LENGTH_SHORT).show();
            isValidate = false;
        } else if (Pattern.matches("^.{3,}@.{3,}$", email) &&
                Pattern.matches("[A-Z_a-z_а-я_А-Я_0-9\\u002E\\u005F]{6,15}", password)) {
            isValidate = true;
        } else isValidate = false;
        return isValidate;
    }

}

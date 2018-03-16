package com.androiddev.artemqa.gototrip.modules.editProfile.view;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.androiddev.artemqa.gototrip.R;

public class EditProfileActivity extends AppCompatActivity {

    TextInputEditText mEtName, mEtCountry, mEtCity, mEtAboutMe, mEtCountyList;
    CardView mCvBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initForm();
    }

    private void initForm() {
        mEtName = findViewById(R.id.et_name_edit_profile_a);
        mEtCountry = findViewById(R.id.et_country_edit_profile_a);
        mEtCity = findViewById(R.id.et_city_edit_profile_a);
        mEtAboutMe = findViewById(R.id.et_about_edit_profile_a);
        mEtCountyList = findViewById(R.id.et_list_country_edit_profile_a);
        mCvBtnSave = findViewById(R.id.cv_btn_save_edit_profile_a);
        mCvBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

package com.androiddev.artemqa.gototrip.common;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.androiddev.artemqa.gototrip.R;

/**
 * Created by artemqa on 15.03.2018.
 */


public class BaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.label_loading_progress_dialog));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}
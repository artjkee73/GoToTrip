package com.androiddev.artemqa.gototrip.modules.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androiddev.artemqa.gototrip.R;


public class SearchActivity extends AppCompatActivity implements ContractSearch.View {

    ContractSearch.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mPresenter = new SearchPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}

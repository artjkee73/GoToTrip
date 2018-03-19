package com.androiddev.artemqa.gototrip.modules.search;

/**
 * Created by artemqa on 19.03.2018.
 */

public interface ContractSearch {
    interface View{
        void showProgress();
        void hideProgress();
    }
    interface Presenter{
        void attachView(View view);

        void detachView();

    }
}

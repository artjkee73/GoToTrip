package com.androiddev.artemqa.gototrip.modules.search;

/**
 * Created by artemqa on 19.03.2018.
 */

public class SearchPresenter  implements ContractSearch.Presenter{
    ContractSearch.View mView;
    SearchInteractor mInteractor;

    public SearchPresenter(){
        mInteractor = new SearchInteractor(this);
    }

    @Override
    public void attachView(ContractSearch.View view) {

    }

    @Override
    public void detachView() {

    }
}

package com.androiddev.artemqa.gototrip.modules.search.presenter;

import com.androiddev.artemqa.gototrip.modules.search.ContractSearch;
import com.androiddev.artemqa.gototrip.modules.search.presenter.SearchInteractor;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 19.03.2018.
 */

public class SearchPresenter  implements ContractSearch.Presenter {
    ContractSearch.View mView;
    SearchInteractor mInteractor;

    public SearchPresenter(){
        mInteractor = new SearchInteractor(this);
    }

    @Override
    public void attachView(ContractSearch.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void onButtonSearchClicked(String userName){
        mInteractor.getQueryUser(userName);
    }

    @Override
    public void onGettingSearchQueryUser(Query userNameQuery) {
        mView.searchUser(userNameQuery);
    }

    @Override
    public void onItemRecyclerViewClicked(String idUserClicked) {
        mView.openViewProfileActivity(idUserClicked);
    }
}

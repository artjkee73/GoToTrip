package com.androiddev.artemqa.gototrip.modules.search.presenter;

import com.androiddev.artemqa.gototrip.modules.search.ContractSearch;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 19.03.2018.
 */

public class SearchInteractor {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mBaseRefDatabase = mDatabase.getReference();

    ContractSearch.Presenter mPresenter;

    public SearchInteractor(ContractSearch.Presenter presenter){
        mPresenter = presenter;
    }

    public void getQueryUser(String userName){
        DatabaseReference usersRef = mBaseRefDatabase.child("users");
        Query searchUserQuery = usersRef.orderByChild("name").startAt(userName).endAt(userName+"\uf8ff");
        mPresenter.onGettingSearchQueryUser(searchUserQuery);

    }

}

package com.androiddev.artemqa.gototrip.modules.search.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.search.ContractSearch;
import com.androiddev.artemqa.gototrip.modules.search.presenter.SearchPresenter;
import com.androiddev.artemqa.gototrip.modules.viewProfile.view.ViewProfileActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


public class SearchActivity extends AppCompatActivity implements ContractSearch.View {
    RecyclerView mRvUsers;
    EditText mEtUsername;
    ImageButton mBtnSearch;
    ContractSearch.Presenter mPresenter;
    FirebaseRecyclerAdapter<User,UserViewHolder> firebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mRvUsers = findViewById(R.id.rv_search_people_search_a);
        mEtUsername = findViewById(R.id.et_search_search_a);
        mBtnSearch = findViewById(R.id.btn_search_search_a);
        mRvUsers = findViewById(R.id.rv_search_people_search_a);
        mRvUsers.setHasFixedSize(true);
        mRvUsers.setLayoutManager(new LinearLayoutManager(this));
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onButtonSearchClicked(mEtUsername.getText().toString());
            }
        });
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
    public void searchUser(Query userNameQuery) {

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(userNameQuery, User.class)
                        .setLifecycleOwner(this)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final User model) {
                holder.mTvName.setText(model.getName());
                holder.mTvCountryCity.setText(model.getCountry().concat(", ").concat(model.getCity()));
                Glide.with(getApplicationContext()).load(model.getUriAvatar()).into(holder.mIvAvatar);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String idUserClicked = model.getUserId();
                        mPresenter.onItemRecyclerViewClicked(idUserClicked);
                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_search_user_item, parent, false);
                return new UserViewHolder(view);
            }

        };
        mRvUsers.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void openViewProfileActivity(String idUserClicked) {
        Intent intent = new Intent(SearchActivity.this, ViewProfileActivity.class);
        intent.putExtra(Constants.INTENT_CLICKED_USER_ID_SEARCH,idUserClicked);
        startActivity(intent);
    }
}

package com.androiddev.artemqa.gototrip.modules.listUsers.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.listUsers.presenter.ListUsersPresenter;
import com.androiddev.artemqa.gototrip.modules.listUsers.ContractListUsers;
import com.androiddev.artemqa.gototrip.modules.search.view.SearchActivity;
import com.androiddev.artemqa.gototrip.modules.search.view.UserViewHolder;
import com.androiddev.artemqa.gototrip.modules.viewProfile.view.ViewProfileActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ListUsersActivity extends AppCompatActivity implements ContractListUsers.View {
    RecyclerView mRvUsers;
    ContractListUsers.Presenter mPresenter;
    FirebaseRecyclerAdapter<User,UserViewHolder> mFirebaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        initView();
    }

    private void initView() {
        mPresenter = new ListUsersPresenter();
        mPresenter.attachView(this);
        mRvUsers = findViewById(R.id.rv_list_users_list_users_a);
        mRvUsers.setLayoutManager(new LinearLayoutManager(this));
        mRvUsers.setHasFixedSize(true);
        mPresenter.viewIsReady(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void loadRV(Query queryKey, DatabaseReference refData) {
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setIndexedQuery(queryKey,refData,User.class)
                        .setLifecycleOwner(this)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final User model) {
                holder.mTvName.setText(model.getName());
                holder.mTvCountryCity.setText(model.getCountry().concat(", ").concat(model.getCity()));
                Glide.with(getApplicationContext()).load(model.getUriAvatar()).into(holder.mIvAvatar);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onItemRvClicked(model.getUserId());
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
        mRvUsers.setAdapter(mFirebaseAdapter);

    }

    @Override
    public void openViewProfile(String idUserClicked) {
        Intent intent = new Intent(ListUsersActivity.this, ViewProfileActivity.class);
        intent.putExtra(Constants.INTENT_CLICKED_USER_ID_SEARCH,idUserClicked);
        startActivity(intent);
    }
}

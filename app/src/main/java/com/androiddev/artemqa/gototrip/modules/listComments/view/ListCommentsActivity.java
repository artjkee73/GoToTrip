package com.androiddev.artemqa.gototrip.modules.listComments.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Comment;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listComments.ContractListComments;
import com.androiddev.artemqa.gototrip.modules.listComments.presenter.ListCommentsPresenter;
import com.androiddev.artemqa.gototrip.modules.search.view.UserViewHolder;
import com.androiddev.artemqa.gototrip.modules.viewProfile.view.ViewProfileActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class ListCommentsActivity extends AppCompatActivity implements ContractListComments.View {
    private ContractListComments.Presenter mPresenter;
    private RecyclerView mRvComments;
    private EditText mEtCommentText;
    private ImageButton mIbSendComment;
    private FirebaseRecyclerAdapter<Comment, CommentViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comments);
        initView();
    }

    private void initView() {
        mPresenter = new ListCommentsPresenter();
        mPresenter.attachView(this);
        mRvComments = findViewById(R.id.rv_comments_list_comments_a);
        mRvComments.setLayoutManager(new LinearLayoutManager(this));
        mIbSendComment = findViewById(R.id.ib_send_comment_list_comments_a);
        mEtCommentText = findViewById(R.id.et_text_comment_list_comments_a);
        mEtCommentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mIbSendComment.setEnabled(true);
                } else mIbSendComment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mIbSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onButtonSendCommentClicked(mEtCommentText.getText().toString(),getPostIdFromIntent());
            }
        });
        mPresenter.viewIsReady(getPostIdFromIntent());
    }

    private String getPostIdFromIntent() {
        return getIntent().getStringExtra(Constants.INTENT_POST_ID);
    }

    @Override
    public void loadRV(Query queryKey, DatabaseReference refData) {

        FirebaseRecyclerOptions<Comment> options =
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setIndexedQuery(queryKey, refData, Comment.class)
                        .setLifecycleOwner(this)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull final Comment model) {
                holder.mTvNameAuthor.setText(model.getNameAuthor());
                holder.mTvDateCreated.setText(Utils.timestampToDateMessage(model.getDateCreatedLong()));
                Glide.with(getApplicationContext()).load(model.getUrlAvatarAuthor()).into(holder.mIvAvatarAuthor);
                holder.mIvAvatarAuthor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onAvatarUserClicked(model.getAuthorId());
                    }
                });
                holder.mTvTextComment.setText(model.getTextComment());
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_comment_item, parent, false);
                return new CommentViewHolder(view);
            }

        };
        mRvComments.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void clearEtComment() {
        mEtCommentText.setText("");
    }

    @Override
    public void openViewProfile(String authorId) {
        Intent intent = new Intent(ListCommentsActivity.this, ViewProfileActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID,authorId);
        startActivity(intent);
    }
}

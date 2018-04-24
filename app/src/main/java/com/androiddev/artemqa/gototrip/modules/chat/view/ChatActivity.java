package com.androiddev.artemqa.gototrip.modules.chat.view;

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
import com.androiddev.artemqa.gototrip.common.models.Chat;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.chat.ContractChat;
import com.androiddev.artemqa.gototrip.modules.chat.presenter.ChatPresenter;
import com.androiddev.artemqa.gototrip.modules.dialog.view.DialogActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class ChatActivity extends AppCompatActivity implements ContractChat.View {
    private RecyclerView mRvChat;
    private ContractChat.Presenter mPresenter;
    private FirebaseRecyclerAdapter<Chat,ChatViewHolder> mFirebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Glide.get(getApplicationContext()).setMemoryCategory(MemoryCategory.HIGH);
        loadView();
    }

    private void loadView() {
        mPresenter = new ChatPresenter();
        mPresenter.attachView(this);
        mRvChat = findViewById(R.id.rv_chat_chat_a);
        mRvChat.setLayoutManager(new LinearLayoutManager(this));
        mRvChat.setHasFixedSize(true);
        mPresenter.viewIsReady();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void updateUI() {
        mPresenter.viewIsReady();
    }

    @Override
    public void loadRv(Query keyRef, DatabaseReference dataRef) {

        FirebaseRecyclerOptions<Chat> options =
                new FirebaseRecyclerOptions.Builder<Chat>()
                        .setIndexedQuery(keyRef,dataRef,Chat.class)
                        .setLifecycleOwner(this)
                        .build();

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull final Chat model) {
                holder.bind(model);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onItemRvClicked(model.getChatId(),model.getMembers());
                    }
                });
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_chat_item, parent, false);
                return new ChatViewHolder(view);
            }
        };

        mRvChat.setAdapter(mFirebaseRecyclerAdapter);

    }

    @Override
    public void openDialog(String chatClickedId, String interlocutorId) {
        Intent intent = new Intent(ChatActivity.this, DialogActivity.class);
        intent.putExtra(Constants.INTENT_CHAT_ID,chatClickedId);
        intent.putExtra(Constants.INTENT_USER_ID,interlocutorId);
        startActivity(intent);
    }
}

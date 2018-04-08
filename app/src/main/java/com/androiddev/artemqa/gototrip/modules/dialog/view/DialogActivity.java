package com.androiddev.artemqa.gototrip.modules.dialog.view;

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
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Message;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.dialog.ContractDialog;
import com.androiddev.artemqa.gototrip.modules.dialog.presenter.DialogPresenter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogActivity extends AppCompatActivity implements ContractDialog.View, View.OnClickListener {
    CircleImageView mIvAvatarInterlocutor;
    RecyclerView mRvMessages;
    TextView mTvNameInterlocutor, mTvEmptyDialog;
    ImageButton mIbSendMessage;
    EditText mEtTextMessage;
    ContractDialog.Presenter mPresenter;
    FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initView();
    }

    private void initView() {
        mPresenter = new DialogPresenter();
        mPresenter.attachView(this);
        mRvMessages = findViewById(R.id.rv_dialog_a);
        mRvMessages.setLayoutManager(new LinearLayoutManager(this));
        mTvEmptyDialog = findViewById(R.id.tv_empty_dialog_dialog_a);
        mIvAvatarInterlocutor = findViewById(R.id.iv_avatar_interlocutor_dialog_a);
        mTvNameInterlocutor = findViewById(R.id.tv_name_interlocutor_dialog_a);
        mIbSendMessage = findViewById(R.id.ib_send_message_dialog_a);
        mIbSendMessage.setOnClickListener(this);
        mEtTextMessage = findViewById(R.id.et_text_message_dialog_a);
        mEtTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mIbSendMessage.setEnabled(true);
                } else mIbSendMessage.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPresenter.viewIsReady(getInterlocutorIdFromIntent());

    }

    private String getInterlocutorIdFromIntent() {
        return getIntent().getStringExtra(Constants.INTENT_DIALOG_USER_ID_VIEW_PROFILE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showEmptyRV() {
        mRvMessages.setVisibility(View.GONE);
        mTvEmptyDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyRV() {
        mRvMessages.setVisibility(View.VISIBLE);
        mTvEmptyDialog.setVisibility(View.GONE);
    }

    @Override
    public void setInterlocutorName(String nameInterlocutor) {
        mTvNameInterlocutor.setText(nameInterlocutor);
    }

    @Override
    public void setAvatarInterlocutor(String urlAvatar) {
        Picasso.get().load(urlAvatar).into(mIvAvatarInterlocutor);
    }

    @Override
    public void updateUI() {
        mPresenter.viewIsReady(getInterlocutorIdFromIntent());
    }

    @Override
    public void loadRvData(Query keyRef, DatabaseReference dataRef, final String currentUserId) {

        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>()
                .setIndexedQuery(keyRef, dataRef, Message.class)
                .setLifecycleOwner(this)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {

                holder.mTvTextMessage.setText(model.getText());
                holder.nTvTimeMessage.setText(String.valueOf(Utils.timestampToDateMessage(model.getDateCreatedLong()) ));
                Picasso.get().load(model.getAuthorUrlAvatar()).into(holder.mCivAvatar);

            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                if (viewType == Constants.RV_DIALOG_SEND_MESSAGE_TYPE) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recycler_view_dialog_item_send, parent, false);
                } else {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recycler_view_dialog_item_recieved, parent, false);
                }
                return new MessageViewHolder(view);
            }

            @Override
            public int getItemViewType(int position) {
                return getItem(position).getAuthorId().equals(currentUserId) ?
                        Constants.RV_DIALOG_SEND_MESSAGE_TYPE : Constants.RV_DIALOG_RECEIVED_MESSAGE_TYPE;
            }
        };
        mRvMessages.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_send_message_dialog_a) {
            mPresenter.onSendMessageClicked(mEtTextMessage.getText().toString());
        }
    }
}

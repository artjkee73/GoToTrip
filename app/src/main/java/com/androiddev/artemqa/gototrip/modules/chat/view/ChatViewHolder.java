package com.androiddev.artemqa.gototrip.modules.chat.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Chat;
import com.androiddev.artemqa.gototrip.common.models.Message;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.chat.ContractChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by artemqa on 08.04.2018.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {

    CircleImageView mIvInterlocutorAvatar, mIvAuthorLastMessageAvatar;
    TextView mTvIntelocutorName, mTvTextLastMessage;

    private ContractChat.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefDatabaseBase = mDatabase.getReference();
    private String mInterlocutorUrlAvatar;
    private String mAuthorUrlAvatar;


    public ChatViewHolder(View itemView) {
        super(itemView);

        mIvInterlocutorAvatar = itemView.findViewById(R.id.iv_avatar_interlocutor_chat_item);
        mIvAuthorLastMessageAvatar = itemView.findViewById(R.id.iv_avatar_last_message_chat_item);
        mTvIntelocutorName = itemView.findViewById(R.id.tv_name_interlocutor_chat_item);
        mTvTextLastMessage = itemView.findViewById(R.id.tv_last_message_chat_chat_item);

    }

    public void bind(Chat currentChat) {

        String mInterlocutorId = Utils.getInterlocutorId(currentChat.getMembers(),mCurrentUser.getUid());
        DatabaseReference refInterlocutor = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(mInterlocutorId);
        refInterlocutor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    User interlocutorUser  = dataSnapshot.getValue(User.class);
                    setAvatarInterlocutor(interlocutorUser.getUriAvatar());
                    setNameInterlocutor(interlocutorUser.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference refLastMessage = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION).child(currentChat.getLastMessageId());
        refLastMessage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Message lastMessage = dataSnapshot.getValue(Message.class);
                    setTextLastMessage(lastMessage.getText());
                    setAvatarAuthorLastMessage(lastMessage.getAuthorUrlAvatar());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setAvatarInterlocutor(String urlAvatarInterlocutor) {
        mInterlocutorUrlAvatar = urlAvatarInterlocutor;
        Utils.loadImage(itemView.getContext(),mInterlocutorUrlAvatar,mIvInterlocutorAvatar);
    }

    private void setNameInterlocutor(String nameInterlocutor) {

        mTvIntelocutorName.setText(nameInterlocutor);
    }

    private void setTextLastMessage(String text) {
        mTvTextLastMessage.setText(text);
    }

    private void setAvatarAuthorLastMessage(String urlAvatar) {
        mAuthorUrlAvatar = urlAvatar;
        Utils.loadImage(itemView.getContext(),mAuthorUrlAvatar,mIvAuthorLastMessageAvatar);
    }
}

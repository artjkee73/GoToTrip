package com.androiddev.artemqa.gototrip.modules.dialog.presenter;

import com.androiddev.artemqa.gototrip.common.models.Chat;
import com.androiddev.artemqa.gototrip.common.models.Message;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.dialog.ContractDialog;
import com.androiddev.artemqa.gototrip.modules.main.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by artemqa on 05.04.2018.
 */

public class DialogInteractor {
    private ContractDialog.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefDatabaseBase = mDatabase.getReference();
    private DatabaseReference refChatsBase = mRefDatabaseBase.child(Constants.CHATS_LOCATION);
    private DatabaseReference refMessagesBase = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION);
    private String interlocutorId;

    public DialogInteractor(ContractDialog.Presenter presenter) {
        mPresenter = presenter;
    }

    public void getInterlocutorAndCurrentUserProfile(final String idInterlocutor) {
        DatabaseReference refInterlocutor = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(idInterlocutor);
        final DatabaseReference refCurrentUser = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        refInterlocutor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    final User interlocutorUser = dataSnapshot.getValue(User.class);
                    interlocutorId = interlocutorUser.getUserId();
                    refCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                User currentUser = dataSnapshot.getValue(User.class);
                                mPresenter.onGettingInterlocutorAndCurrentUser(interlocutorUser, currentUser);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //метод для получения сообщений из чата

    public void getQueryForGetMessages(String currentChatId) {
        Query keyRef = mRefDatabaseBase.child(Constants.CHATS_LOCATION).child(currentChatId).child("messages").orderByKey();
        DatabaseReference dataRef = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION);
        String currentUserId = mCurrentUser.getUid();
        mPresenter.onGettingQueryForGetMessages(keyRef,dataRef,currentUserId);
    }

    public void addMessageInChat(final String currentChatId, final String textMessage) {
        final DatabaseReference currentChatRef = mRefDatabaseBase.child(Constants.CHATS_LOCATION).child(currentChatId);
        final DatabaseReference newMessageRef = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION);
        DatabaseReference currentUserRef = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        final String keyMessage = newMessageRef.push().getKey();
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    User currentUser = dataSnapshot.getValue(User.class);
                    Message newMessage = new Message (keyMessage,mCurrentUser.getUid(),currentChatId,textMessage,currentUser.getUriAvatar());
                    newMessageRef.child(keyMessage).setValue(newMessage);
                    currentChatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot!=null){
                                Chat currentChat = dataSnapshot.getValue(Chat.class);
                                currentChat.getMessages().put(keyMessage,true);
                                currentChat.setLastMessageId(keyMessage);
                                currentChatRef.setValue(currentChat);
                                mPresenter.onNewMessageAdded();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









    }

    //метод для создания чата,добавления сообщения в него, обновления полседнего сообщения и добавления id чатов у обоих собеседников
    public void createChatAndAddMessage(final String textMessage) {

        final String keyChat = refChatsBase.push().getKey();
        final String keyMessage = refMessagesBase.push().getKey();

        final DatabaseReference currentUserRef = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        final DatabaseReference interlocutorUserRef = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(interlocutorId);

        Map<String, Boolean> chatMembers = new HashMap<>();
        chatMembers.put(interlocutorId, true);
        chatMembers.put(mCurrentUser.getUid(), true);

        Map<String, Boolean> chatMessages = new HashMap<>();
        chatMessages.put(keyMessage, true);

        final Chat currentChat = new Chat(keyChat, chatMembers, chatMessages, keyMessage);
        refChatsBase.child(keyChat).setValue(currentChat);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    User currentUser = dataSnapshot.getValue(User.class);
                    String senderUriAvatar = currentUser.getUriAvatar();
                    Message newMessage = new Message(keyMessage, mCurrentUser.getUid(), keyChat, textMessage,senderUriAvatar);
                    refMessagesBase.child(keyMessage).setValue(newMessage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    currentUser.getChats().put(keyChat, true);
                    currentUserRef.setValue(currentUser);
                    interlocutorUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                User interlocutorUser = dataSnapshot.getValue(User.class);
                                interlocutorUser.getChats().put(keyChat, true);
                                interlocutorUserRef.setValue(interlocutorUser);
                                mPresenter.onFirstMessageAdded();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

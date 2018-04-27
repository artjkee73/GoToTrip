package com.androiddev.artemqa.gototrip.modules.dialog.presenter;

import com.androiddev.artemqa.gototrip.common.models.Chat;
import com.androiddev.artemqa.gototrip.common.models.Message;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.dialog.ContractDialog;
import com.androiddev.artemqa.gototrip.modules.main.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
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

    public void addMessageInChat(final String currentChatId, final String textMessage) {
        final DatabaseReference currentChatRef = mRefDatabaseBase.child(Constants.CHATS_LOCATION).child(currentChatId);
        final DatabaseReference newMessageRef = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION);
        DatabaseReference currentUserRef = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        final String keyMessage = newMessageRef.push().getKey();
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    Message newMessage = new Message(keyMessage, mCurrentUser.getUid(), currentChatId, textMessage, currentUser.getUriAvatarThumbnail());
                    newMessageRef.child(keyMessage).setValue(newMessage);
                    currentChatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Chat currentChat = dataSnapshot.getValue(Chat.class);
                                currentChat.getMessages().put(keyMessage, true);
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
                if (dataSnapshot != null) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    String senderUriAvatar = currentUser.getUriAvatarThumbnail();
                    Message newMessage = new Message(keyMessage, mCurrentUser.getUid(), keyChat, textMessage, senderUriAvatar);
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

    public void getInterlocutorUser(String idInterlocutor) {
        DatabaseReference refInterlocutor = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(idInterlocutor);
        refInterlocutor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User interlocutor = dataSnapshot.getValue(User.class);
                    mPresenter.onGettingInterlocutorUser(interlocutor);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getInitialDataForRV(String currentChatId) {
        final ArrayList<String> idMessages = new ArrayList<>();
        final ArrayList<Message> messages = new ArrayList<>();
        Query queryKeys = mRefDatabaseBase.child(Constants.CHATS_LOCATION).child(currentChatId).child("messages").orderByValue().limitToLast(10);
        final DatabaseReference queryMassageBase = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION);
        queryKeys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        idMessages.add(id.getKey());
                    }
                    for (final String idMessage : idMessages) {
                        final DatabaseReference messageRef = queryMassageBase.child(idMessage);
                        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    Message message = dataSnapshot.getValue(Message.class);
                                    messages.add(message);
                                    if (messages.size() == idMessages.size()) {
                                        mPresenter.onLoadInitialDataForAdapter(messages);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getOldDataForRecyclerView(final String lastItemId, String currentChatId) {
        final ArrayList<String> idMessages = new ArrayList<>();
        final ArrayList<Message> messages = new ArrayList<>();
        Query queryKeys = mRefDatabaseBase.child(Constants.CHATS_LOCATION).child(currentChatId).child("messages").orderByKey().endAt(lastItemId).limitToLast(11);
        final DatabaseReference queryMassageBase = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION);
        queryKeys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        if (!id.getKey().equals(lastItemId)) {
                            idMessages.add(id.getKey());
                        }
                    }
                    if (!idMessages.isEmpty()){
                        for (final String idMessage : idMessages) {
                            final DatabaseReference messageRef = queryMassageBase.child(idMessage);
                            messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Message message = dataSnapshot.getValue(Message.class);
                                        messages.add(message);
                                        if (messages.size() == idMessages.size()) {
                                            mPresenter.onLoadOldDataForRV(messages);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                }else mPresenter.onEndOldDataForLoadInRV();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setNewMessageListenerOnRv(String currentChatId, final String lastItemId) {
        DatabaseReference refCurrentChat = mRefDatabaseBase.child(Constants.CHATS_LOCATION).child(currentChatId).child("messages");
        final DatabaseReference refMessagesBase = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION);
        refCurrentChat.orderByKey().startAt(lastItemId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && !dataSnapshot.getKey().equals(lastItemId)) {
                    String messageId = dataSnapshot.getKey();
                    refMessagesBase.child(messageId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Message newMessage = dataSnapshot.getValue(Message.class);
                                mPresenter.onGettingNewMessageForRV(newMessage);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

package com.androiddev.artemqa.gototrip.modules.dialog.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Message;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.ListPostsActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artemqa on 16.04.2018.
 */

public class DialogRecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> mMessages = new ArrayList<>();
    private String mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public DialogRecyclerAdapter(List<Message> messages) {
        mMessages = messages;
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
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.mTvTextMessage.setText(mMessages.get(position).getText());
        holder.nTvTimeMessage.setText(String.valueOf(Utils.timestampToDateMessage(mMessages.get(position).getDateCreatedLong())));
        Glide.with(holder.itemView.getContext()).load(mMessages.get(position).getAuthorUrlAvatar()).into(holder.mCivAvatar);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getAuthorId().equals(mCurrentUserId) ?
                Constants.RV_DIALOG_SEND_MESSAGE_TYPE : Constants.RV_DIALOG_RECEIVED_MESSAGE_TYPE;
    }

    public void addItems(List<Message> newMessage) {
        mMessages.addAll(0, newMessage);
        notifyItemRangeInserted(0, newMessage.size());
    }

    public String getFirstItemId() {
        return mMessages.get(0).getMessageId();
    }

    public String getLastItemId() {
        return mMessages.get(mMessages.size() - 1).getMessageId();
    }

    public void addNewItem(Message message) {
        int initialSize = mMessages.size();
        mMessages.add(message);
        notifyItemInserted(initialSize);
    }
}

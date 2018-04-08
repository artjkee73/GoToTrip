package com.androiddev.artemqa.gototrip.modules.dialog.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by artemqa on 06.04.2018.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    CircleImageView mCivAvatar;
    TextView mTvTextMessage, nTvTimeMessage;

    public MessageViewHolder(View itemView) {
        super(itemView);
        mCivAvatar = itemView.findViewById(R.id.iv_avatar_user_rv_dialog_i);
        mTvTextMessage = itemView.findViewById(R.id.tv_text_message_rv_dialog_i);
        nTvTimeMessage = itemView.findViewById(R.id.tv_date_message_rv_dialog_i);
    }
}

package com.androiddev.artemqa.gototrip.modules.listComments.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by artemqa on 11.04.2018.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

   public CircleImageView mIvAvatarAuthor;
   public TextView mTvNameAuthor,mTvDateCreated,mTvTextComment;
    public CommentViewHolder(View itemView) {
        super(itemView);
        mIvAvatarAuthor = itemView.findViewById(R.id.iv_avatar_author_rv_comment_item);
        mTvNameAuthor = itemView.findViewById(R.id.tv_name_author_rv_comment_item);
        mTvDateCreated = itemView.findViewById(R.id.tv_date_comment_rv_comment_item);
        mTvTextComment = itemView.findViewById(R.id.tv_text_comment_rv_comment_item);
    }
}

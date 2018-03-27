package com.androiddev.artemqa.gototrip.modules.search.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by artjk on 20.03.2018.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {
    CircleImageView mIvAvatar;
    TextView mTvName, mTvCountryCity;

    public UserViewHolder(View itemView) {
        super(itemView);
        mIvAvatar = itemView.findViewById(R.id.iv_avatar_user_i_rv_search);
        mTvName = itemView.findViewById(R.id.tv_name_user_i_rv_search);
        mTvCountryCity = itemView.findViewById(R.id.tv_country_city_i_rv_search);
    }
}

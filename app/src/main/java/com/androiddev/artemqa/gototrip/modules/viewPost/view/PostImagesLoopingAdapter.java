package com.androiddev.artemqa.gototrip.modules.viewPost.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Photo;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnPostPhotoInRecyclerViewPostsClickListener;
import com.asksira.loopingviewpager.LoopingPagerAdapter;
import java.util.ArrayList;


public class PostImagesLoopingAdapter extends LoopingPagerAdapter<Photo> {
    private OnPostPhotoInRecyclerViewPostsClickListener mOnPostPhotoInRecyclerViewPostsClickListener;
    public PostImagesLoopingAdapter(Context context, ArrayList<Photo> itemList, boolean isInfinite , OnPostPhotoInRecyclerViewPostsClickListener onPostPhotoInRecyclerViewPostsClickListener) {
        super(context, itemList, isInfinite);
        mOnPostPhotoInRecyclerViewPostsClickListener = onPostPhotoInRecyclerViewPostsClickListener;
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.looping_image_item, container, false);

    }
    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        ImageView imagePost =  convertView.findViewById(R.id.iv_image_asymmetric_grid_view);
        Utils.loadImage(context,getItem(listPosition).getPhotoThumbnailUrl(),imagePost);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPostPhotoInRecyclerViewPostsClickListener.onPostPhotoClicked(getItem(listPosition).getPhotoOriginalUrl());
            }
        });
    }

}
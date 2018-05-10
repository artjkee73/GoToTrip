package com.androiddev.artemqa.gototrip.modules.newPost.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.newPost.interfaces.OnAddImageClickListener;
import com.androiddev.artemqa.gototrip.modules.newPost.interfaces.OnImageClickListener;
import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class AddPostImagesGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Uri> mUrisImages = new ArrayList<>();
    private OnAddImageClickListener mOnAddImageClickListener;
    private OnImageClickListener mOnImageClickListener;

    public AddPostImagesGridAdapter(Context c) {
        mContext = c;
        mUrisImages.add(Utils.getUriStringFromResource(mContext, R.drawable.ic_add));

    }

    public void setOnAddImageClickListener(OnAddImageClickListener onAddImageClickListener) {
        mOnAddImageClickListener = onAddImageClickListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    @Override
    public int getCount() {
        return mUrisImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrisImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItems(List<Uri> newItems) {
        mUrisImages.addAll(newItems);
        notifyDataSetChanged();
        if (mUrisImages.size() > 5) {
            mUrisImages.remove(Utils.getUriStringFromResource(mContext, R.drawable.ic_add));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_view_images_item, null);
        }

        final ImageView imageView = convertView.findViewById(R.id.iv_image_images_grid_view);
        Glide.with(parent.getContext()).load(mUrisImages.get(position)).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUrisImages.get(position).equals(Utils.getUriStringFromResource(mContext, R.drawable.ic_add))) {
                    mOnAddImageClickListener.onAddImageClicked(Constants.MAX_PICK_IMAGE_IN_POST - (getCount() - 1));
                } else {
                    mOnImageClickListener.OnItemClicked(position);
                }
            }
        });

        return convertView;
    }

    public List<String> getItemsFromAdapter() {
        List<String> images = new ArrayList<>();
        for (Uri uriImg : mUrisImages) {
            if (Utils.getPath(mContext, uriImg) != null) {
                images.add(Utils.getPath(mContext, uriImg));
            }
        }
        return images;
    }

    public void removeImage(int itemPosition) {
        mUrisImages.remove(itemPosition);
        notifyDataSetChanged();
        if (mUrisImages.size() < 5 && !mUrisImages.contains(Utils.getUriStringFromResource(mContext, R.drawable.ic_add))) {
            mUrisImages.add(Utils.getUriStringFromResource(mContext, R.drawable.ic_add));
        }
    }
}
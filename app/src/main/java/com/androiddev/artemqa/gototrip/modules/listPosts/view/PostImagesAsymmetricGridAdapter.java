package com.androiddev.artemqa.gototrip.modules.listPosts.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Photo;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnPostPhotoInRecyclerViewPostsClickListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostImagesAsymmetricGridAdapter extends ArrayAdapter<Photo> {
    public PostImagesAsymmetricGridAdapter(@NonNull Context context, @NonNull List<Photo> items ) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.asymmetric_grid_image, parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.iv_image_asymmetric_grid_view);
        Picasso.get().load(getItem(position).getPhotoThumbnailUrl()).into(imageView);
//        Utils.loadImage(getContext(),getItem(position).getPhotoThumbnailUrl(),imageView);
        return convertView;
    }
}

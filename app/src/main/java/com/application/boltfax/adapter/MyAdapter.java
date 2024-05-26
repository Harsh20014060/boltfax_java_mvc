package com.application.boltfax.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.application.boltfax.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends PagerAdapter {

    ArrayList<String> list;

    public MyAdapter(ArrayList<String> imagelist) {
        this.list = imagelist;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_layout, container, false);
        ImageView imageView = view.findViewById(R.id.imageview);

        try {
            Picasso.get()
                    .load(list.get(position))
                    .fit()
                    .into(imageView);
        } catch (Exception ignored) {

        }


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
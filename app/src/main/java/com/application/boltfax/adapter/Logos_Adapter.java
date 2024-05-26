package com.application.boltfax.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.boltfax.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Logos_Adapter extends RecyclerView.Adapter<Logos_Adapter.LogoViewHolder> {
    ArrayList<String> datalist;
    Context c;
    int i = 1;

    public Logos_Adapter(ArrayList<String> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public LogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        LayoutInflater abc = LayoutInflater.from(parent.getContext()); // parent (kispe shoe hoa h)
        View view = abc.inflate(R.layout.logo_cards, parent, false); // kisko show krna he card.xml
        // inflate krne sara data apna view me store ho gaya

        // sara data apan abcviewholder me pass krdia
        return new LogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogoViewHolder holder, int position) {


        String image_url = datalist.get(position);


        try {
            Picasso.get()
                    .load(image_url)
                    .resize(1335, 460)

                    .into(holder.imageView);
        } catch (Exception ignored) {


        }

    }

    @Override
    public int getItemCount() {

        return datalist.size();
    }

    public class LogoViewHolder extends RecyclerView.ViewHolder {

        //        LinearLayout llt
        ImageView imageView;

        public LogoViewHolder(@NonNull View itemView) {
            super(itemView);

//            llt = itemView.findViewById(R.id.backgroud_color);
            imageView = itemView.findViewById(R.id.logoimages);


        }
    }
}

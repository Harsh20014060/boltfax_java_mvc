package com.application.boltfax.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.boltfax.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrendingDeals_Adapter extends RecyclerView.Adapter<TrendingDeals_Adapter.TrendingViewHolder> {
    ArrayList<ArrayList<String>> datalist;
    Context c;
    int i = 1;

    public TrendingDeals_Adapter(ArrayList<ArrayList<String>> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        LayoutInflater abc = LayoutInflater.from(parent.getContext()); // parent (kispe shoe hoa h)
        View view = abc.inflate(R.layout.deals_card, parent, false); // kisko show krna he card.xml
        // inflate krne sara data apna view me store ho gaya

        // sara data apan abcviewholder me pass krdia
        return new TrendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        holder.pb.setVisibility(View.VISIBLE);
        String[] colors = {
                "#E0FFE9",
                "#F6FFDE",
                "#FFDEDF",
                "#E0EBEB",
                "#DEFFEA",
                "#E0F7FF",
                "#F0E4F7",
                "#F5E6BC",
                "#FFF2E6",
                "#B3FFFF"};
        String image_url = datalist.get(position).get(0);
        String pd_name = datalist.get(position).get(1).trim();
        String pd_rate = datalist.get(position).get(2);
        String pr_discount = datalist.get(position).get(3);


        if (i < colors.length) {

            holder.imageView.setBackgroundColor(Color.parseColor(colors[i]));
            i++;
        } else {
            i = 0;

            holder.imageView.setBackgroundColor(Color.parseColor(colors[i]));
            i = 1;
        }

        try {
            Picasso.get()
                    .load(image_url)
                    .resize(500, 500)
                    .centerCrop()
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } catch (Exception e) {

        }

        holder.tv1.setText(String.format(pd_name));
        holder.tv2.setText(String.format("₹ %s", pd_rate));
        holder.tv3.setText(String.format("%s%% Off", pr_discount));

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class TrendingViewHolder extends RecyclerView.ViewHolder {

        //        LinearLayout llt
        ImageView imageView;
        ProgressBar pb;
        TextView tv1, tv2, tv3;

        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);

//            llt = itemView.findViewById(R.id.backgroud_color);
            pb = itemView.findViewById(R.id.progressBar);
            imageView = itemView.findViewById(R.id.productimage);
            tv1 = itemView.findViewById(R.id.productnaming);
            tv2 = itemView.findViewById(R.id.productprice);
            tv3 = itemView.findViewById(R.id.productoff);


        }
    }
}

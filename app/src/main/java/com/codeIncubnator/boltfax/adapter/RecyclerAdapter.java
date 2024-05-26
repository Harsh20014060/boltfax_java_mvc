package com.codeIncubnator.boltfax.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeIncubnator.boltfax.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ProductViewHolder> {

    ArrayList<ArrayList<String>> datalist;
    Context c;
    int i = 1;

    public RecyclerAdapter(ArrayList<ArrayList<String>> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        LayoutInflater abc = LayoutInflater.from(parent.getContext()); // parent (kispe shoe hoa h)
        View view = abc.inflate(R.layout.product_card, parent, false); // kisko show krna he card.xml
        // inflate krne sara data apna view me store ho gaya

        // sara data apan abcviewholder me pass krdia
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        String[] colors = {"#E0F7FF",
                "#F0E4F7",
                "#F5E6BC",
                "#FFF2E6",
                "#B3FFFF",
                "#E0FFE9",
                "#F6FFDE",
                "#FFDEDF",
                "#E0EBEB",
                "#DEFFEA"};
        String image_url = datalist.get(position).get(0);
        String pd_name = datalist.get(position).get(1).trim();
        String pd_rate = datalist.get(position).get(2);
        String pr_discount = datalist.get(position).get(3);


        if (i < colors.length) {

            holder.llt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colors[i])));
            i++;
        } else {
            i = 0;

            holder.llt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colors[i])));
            i = 1;
        }
        Picasso.get()
                .load(image_url)
                .into(holder.imageView);
        holder.tv1.setText(pd_name);

        holder.tv2.setText("₹ " + pd_rate);
        holder.tv3.setText("After " + pr_discount + "% Off");

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llt;
        ImageView imageView;
        TextView tv1, tv2, tv3;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            llt = itemView.findViewById(R.id.backgroud_color);
            imageView = itemView.findViewById(R.id.imageView1);
            tv1 = itemView.findViewById(R.id.productName);
            tv2 = itemView.findViewById(R.id.price);
            tv3 = itemView.findViewById(R.id.discount);


        }
    }
}

package com.codeIncubnator.boltfax.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeIncubnator.boltfax.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Category_adapter extends RecyclerView.Adapter<Category_adapter.CategoryViewHolder> {
    ArrayList<ArrayList<String>> datalist;
    Context c;

    public Category_adapter(ArrayList<ArrayList<String>> datalist) {


        this.datalist = datalist;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        LayoutInflater abc = LayoutInflater.from(parent.getContext()); // parent (kispe shoe hoa h)
        View view = abc.inflate(R.layout.category_card_design, parent, false); // kisko show krna he card.xml
        // inflate krne sara data apna view me store ho gaya

        // sara data apan abcviewholder me pass krdia
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {


        String image_url = datalist.get(position).get(0);
        String category_name = datalist.get(position).get(1).trim();
        String category_description = datalist.get(position).get(2);


        Picasso.get()
                .load(image_url)
                .resize(500, 500)
                .centerCrop()
                .into(holder.imageView);


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent av = new Intent(c, com.codeIncubnator.boltfax.ProductList.class);
                av.putExtra("intent_name", "category");
                av.putExtra("categoryname", datalist.get(holder.getLayoutPosition()).get(3));
                c.startActivity(av);
            }
        });
        holder.tv1.setText(category_name);
        holder.tv2.setText(category_description);

    }


    @Override
    public int getItemCount() {

        return datalist.size();

    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        //        LinearLayout llt
        ImageView imageView;
        TextView tv1, tv2;
        Button btn;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            

//            llt = itemView.findViewById(R.id.backgroud_color);
            imageView = itemView.findViewById(R.id.category_imgview);
            tv1 = itemView.findViewById(R.id.categoryame);
            tv2 = itemView.findViewById(R.id.category_description);
            btn = itemView.findViewById(R.id.see_all);


        }
    }
}
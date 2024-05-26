package com.application.boltfax;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.boltfax.adapter.RecyclerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductList extends AppCompatActivity {

    FloatingActionButton sort, home;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView tv;
    LinearLayoutManager horizontalmanager;

    RecyclerAdapter adapter;
    FirebaseFirestore db;
    String productname = "";
    ArrayList<String> abc = new ArrayList<>();
    ArrayList<ArrayList<String>> showpdlist = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> sortedarray = new ArrayList<ArrayList<String>>();
    String[] DocumentNameMatchCAse = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        sort = findViewById(R.id.filter_price);
        home = findViewById(R.id.go_to_home);
        recyclerView = findViewById(R.id.recyclerview);

        tv = findViewById(R.id.textView7);
        toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("BoltFax");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        horizontalmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalmanager);


        showpdlist = new ArrayList<ArrayList<String>>();

        adapter = new RecyclerAdapter(showpdlist);

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();


        Intent ab = getIntent();
        productname = ab.getStringExtra("productname");
        tv.setText("Showing results for " + productname);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductList.this, com.application.boltfax.MainActivity.class));
            }
        });


        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> temp = new ArrayList<>();
                for (int i = 0; i < showpdlist.size(); i++) {
                    temp.add(Integer.parseInt(showpdlist.get(i).get(2)));
                }


                Collections.sort(temp);

                for (int j = 0; j < temp.size(); j++) {
                    for (int k = 0; k < showpdlist.size(); k++) {

                        if (temp.get(j).toString().equals(showpdlist.get(k).get(2))) {

                            sortedarray.add(new ArrayList<>(Arrays.asList(showpdlist.get(k).get(0)
                                    , showpdlist.get(k).get(1)
                                    , showpdlist.get(k).get(2)
                                    , showpdlist.get(k).get(3))));
                            showpdlist.remove(k);
                        }
                    }
                }


                adapter = new RecyclerAdapter(sortedarray);

                recyclerView.setAdapter(adapter);


            }
        });

        if (ab.getStringExtra("intent_name").equals("searchpage")) {
            String[] categoryList = {"earbuds", "earphone", "headphones", "laptop", "mobile", "neckband"};
            abc.remove(ab.getStringExtra("categoryname"));
            abc.add(0, ab.getStringExtra("categoryname"));
            for (int lol = 0; lol < categoryList.length; lol++) {
                if (!abc.contains(categoryList[lol])) {
                    abc.add(categoryList[lol]);
                }
            }
            DocumentNameMatchCAse = ab.getStringExtra("documentname").split("_");
        } else {
            abc.add(ab.getStringExtra("categoryname"));

        }


        retrieve_data();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void retrieve_data() {

        final int[] checking = {0};

        final int[] l = {0};


        for (int series = 0; series < abc.size(); series++) {

            int finalSeries = series;
            db.collection(abc.get(series))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {


                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {

                                String[] matchname = d.getId().split("_");


                                if (abc.size() > 1) {
//                                    THIS IS USED WHEN WE SEARCH USING SEARCH PAGE and EXTRACT ALL CATEGORIES
                                    if (matchname[0].contains(DocumentNameMatchCAse[0])) {


                                        l[0]++;
                                        String product_name = String.valueOf(d.get("productname"));
                                        String image_url = String.valueOf(d.get("image"));
                                        String selling_price = String.valueOf(d.get("sellingprice"));
                                        String total_discount = String.valueOf(d.get("discount"));


                                        if (product_name.equals(productname)) {
                                            showpdlist.add(0, new ArrayList<>(Arrays.asList(image_url,
                                                    product_name,
                                                    selling_price,
                                                    total_discount)));
                                        } else {
                                            showpdlist.add(new ArrayList<>(Arrays.asList(image_url,
                                                    product_name,
                                                    selling_price,
                                                    total_discount)));
                                        }

                                        adapter.notifyDataSetChanged();


                                        checking[0] = 1;

                                    }
                                } else {
//                                    THIS CASE IS MADE FOR WHEN PRODUCT SHOW USING CATEGORY

                                    String product_name = String.valueOf(d.get("productname"));
                                    String image_url = String.valueOf(d.get("image"));
                                    String selling_price = String.valueOf(d.get("sellingprice"));
                                    String total_discount = String.valueOf(d.get("discount"));
                                    showpdlist.add(new ArrayList<>(Arrays.asList(image_url,
                                            product_name,
                                            selling_price,
                                            total_discount)));
                                    adapter.notifyDataSetChanged();
                                }


                            }

                            if (showpdlist.size() == l[0]) {
//                                showData();
                            }


                        }

                    });


        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.search) {
            Intent search = new Intent(ProductList.this, com.application.boltfax.SearchPage.class);
            startActivity(search);
        }
        if (i == R.id.notification) {
            Intent notify = new Intent(ProductList.this, com.application.boltfax.Notifications.class);
            startActivity(notify);
        }
        return super.onOptionsItemSelected(item);
    }


}
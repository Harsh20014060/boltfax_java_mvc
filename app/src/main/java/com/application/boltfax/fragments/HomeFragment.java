package com.application.boltfax.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.boltfax.R;
import com.application.boltfax.adapter.DealsOfTheDay_Adapter;
import com.application.boltfax.adapter.Logos_Adapter;
import com.application.boltfax.adapter.MyAdapter;
import com.application.boltfax.adapter.TrendingDeals_Adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_PARAM1 = "param1";

    RecyclerView recyclerViewofdeal, recyclerViewoftrend, recyclerViewoflogos;
    DealsOfTheDay_Adapter deal_adapter;
    TrendingDeals_Adapter trend_adapter;
    Logos_Adapter logo_adapter;
    LinearLayoutManager horizontalmanager, horizontalmanager2;

    com.application.boltfax.WrapContentHeightViewPager vp;
    ImageView internet_connection;
    ArrayList<String> imagelist = new ArrayList<>();
    DotsIndicator dt;
    MyAdapter myAdapter;
    Timer timer;
    Handler handler;
    NestedScrollView sv;
    FloatingActionButton scrolltotop;
    FirebaseFirestore db;
    ArrayList<ArrayList<String>> dealoftheday = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> trendingdeals = new ArrayList<ArrayList<String>>();
    ArrayList<String> brandlogos = new ArrayList<String>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        internet_connection = v.findViewById(R.id.internet_error);

        if (connection()) {
            ViewGroup.LayoutParams params = internet_connection.getLayoutParams();
            params.height = 0;
            params.width = 0;
            internet_connection.setLayoutParams(params);

            dt = v.findViewById(R.id.dots_indicator);
            vp = v.findViewById(R.id.veiwpager);
            sv = v.findViewById(R.id.scrollviewhome);
            scrolltotop = v.findViewById(R.id.scrollupbtn);

            recyclerViewofdeal = v.findViewById(R.id.recyclerview_for_deal);
            recyclerViewoftrend = v.findViewById(R.id.recyclerview_for_trend);
            recyclerViewoflogos = v.findViewById(R.id.recyclerview_for_logo);

            db = FirebaseFirestore.getInstance();

            horizontalmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            horizontalmanager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


            recyclerViewofdeal.setLayoutManager(horizontalmanager);
            recyclerViewoftrend.setLayoutManager(horizontalmanager2);
            recyclerViewoflogos.setLayoutManager(new LinearLayoutManager(getActivity()));

            dealoftheday.add(new ArrayList<>(Arrays.asList("", " ", " ", " ")));
            dealoftheday.add(new ArrayList<>(Arrays.asList("", "", "", "")));
            trendingdeals.add(new ArrayList<>(Arrays.asList("", "boat Rockerz 338", "1699", "43")));
            trendingdeals.add(new ArrayList<>(Arrays.asList("", "boAt Rockerz 330", "1099", "43")));

            brandlogos.add(" ");

            deal_adapter = new DealsOfTheDay_Adapter(dealoftheday);
            trend_adapter = new TrendingDeals_Adapter(trendingdeals);
            logo_adapter = new Logos_Adapter(brandlogos);

            recyclerViewofdeal.setAdapter(deal_adapter);
            recyclerViewoftrend.setAdapter(trend_adapter);
            recyclerViewoflogos.setAdapter(logo_adapter);

            scrolltotop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sv.fullScroll(View.FOCUS_UP);

                }
            });

            imagelist.add(" ");
            imagelist.add(" ");
            imagelist.add(" ");
            imagelist.add(" ");

            db.collection("homescreen")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {

                            int op = 0;
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                Map<String, Object> images = d.getData();
                                for (String keys : images.keySet()) {
                                    imagelist.add(op, String.valueOf(images.get(keys)));
                                    myAdapter.notifyDataSetChanged();
                                }

                                myAdapter.notifyDataSetChanged();
                                try {
                                    int size = imagelist.size();
                                    int initial_size = images.keySet().size();
                                    for (int lol = initial_size; lol <= size; lol++) {
                                        imagelist.remove(initial_size);
                                        myAdapter.notifyDataSetChanged();
                                    }
                                } catch (Exception ignored) {

                                }

                            }
                        }
                    });


            retrieve_logo();
            retrieve_data();

            myAdapter = new MyAdapter(imagelist);
            vp.setAdapter(myAdapter);
            dt.setViewPager(vp);

            handler = new Handler();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new TimerTask() {
                        @Override
                        public void run() {
                            int i = vp.getCurrentItem();
                            if (i == imagelist.size() - 1)
                                vp.setCurrentItem(0, false);
                            else {
                                i++;
                                vp.setCurrentItem(i, true);
                            }
                        }
                    });
                }
            }, 7000, 7000);


        } else {
            internet_connection.setImageResource(R.drawable.connection_error);
            internet_connection.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        return v;


//        return null;


    }

    private void retrieve_data() {
        String[] categoryList = {"earbuds", "earphone", "headphones", "laptop", "mobile", "neckband"};

        for (int series = 0; series < categoryList.length; series++) {

            int finalSeries = series;
            db.collection(categoryList[series])
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {


                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            int op = 0;
                            int po = 0;
                            for (DocumentSnapshot d : list) {

                                String product_name = String.valueOf(d.get("productname"));
                                String image_url = String.valueOf(d.get("image"));
                                String selling_price = String.valueOf(d.get("sellingprice"));
                                String total_discount = String.valueOf(d.get("discount")).trim();
                                String rating = String.valueOf(d.get("discount")).trim();

                                if (Integer.parseInt(total_discount) >= 70) {
                                    if (op == 0) {
                                        dealoftheday.remove(0);
                                        dealoftheday.remove(0);
                                    }

                                    dealoftheday.add(op, new ArrayList<>(Arrays.asList(image_url,
                                            product_name,
                                            "₹ " + selling_price,
                                            total_discount + "% off")));
                                    op++;

                                }
                                if (Integer.parseInt(rating) >= 4) {
                                    if (po == 0) {
                                        trendingdeals.remove(0);
                                        trendingdeals.remove(0);
                                    }

                                    trendingdeals.add(po, new ArrayList<>(Arrays.asList(image_url,
                                            product_name,
                                            selling_price,
                                            total_discount)));
                                    po++;

                                }

                                deal_adapter.notifyDataSetChanged();
                                trend_adapter.notifyDataSetChanged();

                            }


                        }


                    });


        }


    }

    private void retrieve_logo() {

        db.collection("brandlogos")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {


                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        int op = 0;
                        for (DocumentSnapshot d : list) {

                            String image_url = String.valueOf(d.get("image"));


                            if (op == 0) {
                                brandlogos.remove(0);
                            }
                            brandlogos.add(op, image_url);
                            op++;


                            logo_adapter.notifyDataSetChanged();

                        }
                        logo_adapter.notifyDataSetChanged();


                    }

                });


    }

    public boolean connection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        return connected;
    }


}


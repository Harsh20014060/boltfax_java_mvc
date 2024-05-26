package com.codeIncubnator.boltfax.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeIncubnator.boltfax.R;
import com.codeIncubnator.boltfax.adapter.Category_adapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBERf

    AdView mAdView;
    RecyclerView recyclerViewofcategory;
    ArrayList<ArrayList<String>> category = new ArrayList<ArrayList<String>>();
    FirebaseFirestore db;
    Category_adapter categoryAdapter;
    // TODO: Rename and change types of parameters


    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerViewofcategory = v.findViewById(R.id.product_category);
        db = FirebaseFirestore.getInstance();

        retrieve_data();
        categoryAdapter = new Category_adapter(category);
        recyclerViewofcategory.setLayoutManager(new LinearLayoutManager(getActivity()));

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);


                System.out.println("LoadAdError....." + loadAdError);
            }
        });

        return v;
    }

    private void retrieve_data() {
        String[] categoryList = {"category"};

        for (int series = 0; series < categoryList.length; series++) {

            int finalSeries = series;
            db.collection(categoryList[series])
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {


                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {

                                String image_url = String.valueOf(d.get("image"));
                                String cat_name = String.valueOf(d.get("Name"));
                                String cat_desc = String.valueOf(d.get("Desc"));
                                String original_cat_name = d.getId();


                                category.add(new ArrayList<>(Arrays.asList(image_url, cat_name, cat_desc, original_cat_name)));

                                recyclerViewofcategory.setAdapter(categoryAdapter);
                                categoryAdapter.notifyDataSetChanged();

                            }


                        }


                    });


        }


    }
}
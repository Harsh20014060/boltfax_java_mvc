package com.codeIncubnator.boltfax.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codeIncubnator.boltfax.R;
import com.codeIncubnator.boltfax.UnderDevelopment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    com.codeIncubnator.boltfax.MyDataBase myDataBase;
    TextView username;
    AdView menu_AdView;

    LinearLayout choosenlanguage, paymentdetails, mywallet, mywishlist, myorders, chashbackpoint, privacy_and_policy, termsandconditions, helpandsupport, feedbackAndSuggestion, ourstory, signout, setting;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        username = v.findViewById(R.id.username);
        menu_AdView = v.findViewById(R.id.menu_adView);
        choosenlanguage = v.findViewById(R.id.choosenlanguage);
        paymentdetails = v.findViewById(R.id.paymentdetails);
        mywallet = v.findViewById(R.id.mywallet);
        mywishlist = v.findViewById(R.id.mywhishlist);
        myorders = v.findViewById(R.id.myorders);
        chashbackpoint = v.findViewById(R.id.mychashbbackpoint);
        privacy_and_policy = v.findViewById(R.id.privacyAndPolicy);
        termsandconditions = v.findViewById(R.id.termsandcondition);
        helpandsupport = v.findViewById(R.id.helpAndSupport);
        feedbackAndSuggestion = v.findViewById(R.id.feedbackAndSuggestion);
        ourstory = v.findViewById(R.id.ourStory);
        setting = v.findViewById(R.id.setting);
        signout = v.findViewById(R.id.signOut);

        myDataBase = new com.codeIncubnator.boltfax.MyDataBase(getActivity());

        Cursor cursor = myDataBase.getAllInfo();
        if (cursor.getCount() == 0) {
            username.setText("Hello Username");
        } else {
            String ab = null;
            while (cursor.moveToNext()) {

                ab = cursor.getString(0);
                String[] name = ab.split(" ");
                ab = name[0];
            }
            username.setText("Hello " + ab.substring(0, 1).toUpperCase() + ab.substring(1));
        }

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        menu_AdView = v.findViewById(R.id.menu_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        menu_AdView.loadAd(adRequest);

        menu_AdView.setAdListener(new AdListener() {
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


        choosenlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc = new Intent(getActivity(), UnderDevelopment.class);
                abc.putExtra("title", "Choosen Language");
                startActivity(abc);
            }
        });
        paymentdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc = new Intent(getActivity(), UnderDevelopment.class);
                abc.putExtra("title", "Payment Details");
                startActivity(abc);
            }
        });
        mywallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc = new Intent(getActivity(), UnderDevelopment.class);
                abc.putExtra("title", "My Wallet");
                startActivity(abc);
            }
        });
        mywishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc = new Intent(getActivity(), UnderDevelopment.class);
                abc.putExtra("title", "My Wishlist");
                startActivity(abc);
            }
        });
        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc = new Intent(getActivity(), UnderDevelopment.class);
                abc.putExtra("title", "My Orders");
                startActivity(abc);
            }
        });
        chashbackpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc = new Intent(getActivity(), UnderDevelopment.class);
                abc.putExtra("title", "CashBack Point");
                startActivity(abc);
            }
        });

        privacy_and_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), com.codeIncubnator.boltfax.PrivacyAndPolicy.class));
            }
        });
        termsandconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), com.codeIncubnator.boltfax.TermAndConditions.class));
            }
        });
        helpandsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc = new Intent(getActivity(), UnderDevelopment.class);
                abc.putExtra("title", "Help And Support");
                startActivity(abc);
            }
        });
        feedbackAndSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent abc = new Intent(getActivity(), com.codeIncubnator.boltfax.Feedack_And_Suggestion.class);

                startActivity(abc);
            }
        });

        ourstory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), com.codeIncubnator.boltfax.OurStory.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc = new Intent(getActivity(), UnderDevelopment.class);
                abc.putExtra("title", "Setting");
                startActivity(abc);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDataBase.dell_all();
                startActivity(new Intent(getActivity(), com.codeIncubnator.boltfax.LoginPage.class));
                getActivity().finish();
            }
        });


        return v;
    }
}
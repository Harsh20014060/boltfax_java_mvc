package com.codeIncubnator.boltfax;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {

    AutoCompleteTextView act;
    ImageButton clearbtn, backbtn;
    FirebaseFirestore db;
    String pdlist = "";
    ArrayList<String> prodlist;
    String categorylistStr = "";
    String documentlistStr = "";
    ArrayList<String> categorylist;
    ArrayList<String> documentlist;
    ArrayAdapter<String> obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        act = findViewById(R.id.autoCompleteTextView);
        clearbtn = findViewById(R.id.imageButton3);
        backbtn = findViewById(R.id.imageButton);

        db = FirebaseFirestore.getInstance();

        retrieve_data();
        act.setThreshold(1);

        prodlist = new ArrayList<>();


        act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = String.valueOf(parent.getItemAtPosition(position));

                int k = 0;
                for (int y = 0; y < prodlist.size(); y++) {
                    if (prodlist.get(y).equals(name)) {
                        k = y;
                    }
                }


                Intent showproduct = new Intent(SearchPage.this, com.codeIncubnator.boltfax.ProductList.class);
                showproduct.putExtra("categoryname", categorylist.get(k));
                showproduct.putExtra("documentname", documentlist.get(k));
                showproduct.putExtra("productname", name);
                showproduct.putExtra("intent_name", "searchpage");
                startActivity(showproduct);
            }
        });

        act.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Intent abc = new Intent(SearchPage.this, UnderDevelopment.class);
                abc.putExtra("title", "Manual Search");
                startActivity(abc);
                return false;
            }
        });

        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.setText(null);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(SearchPage.this, MainActivity.class);
                startActivity(goback);
            }
        });


    }

    private void retrieve_data() {
        prodlist = new ArrayList<>();
        categorylist = new ArrayList<>();
        documentlist = new ArrayList<>();
        String[] collectionList = {"earbuds", "earphone", "headphones", "laptop", "mobile", "neckband"};
        final int[] l = {0};
        for (int series = 0; series < collectionList.length; series++) {

            int finalSeries = series;
            db.collection(collectionList[series])
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {
                                if (String.valueOf(d.get("productname")).length() > 1) {
                                    prodlist.add(String.valueOf(d.get("productname")));
                                    categorylist.add(collectionList[finalSeries]);
                                    documentlist.add(String.valueOf(d.getId()));
                                }

                            }


                            obj = new ArrayAdapter<String>(SearchPage.this,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    prodlist);

                            act.setAdapter(obj);
                            obj.notifyDataSetChanged();

                        }
                    });
        }


    }

}
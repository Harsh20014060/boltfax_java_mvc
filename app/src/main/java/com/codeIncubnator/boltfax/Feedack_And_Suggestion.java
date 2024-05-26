package com.codeIncubnator.boltfax;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Feedack_And_Suggestion extends AppCompatActivity {

    TextView text;
    ImageButton backbutton;
    Button submit;
    EditText feedback;
    com.codeIncubnator.boltfax.MyDataBase dataBase;
    ArrayList<String> agh = new ArrayList<>();
    FirebaseFirestore db;
    ProgressDialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedack_and_suggestion);

        text = findViewById(R.id.universal_backstrip_text);
        backbutton = findViewById(R.id.universal_backstrip_backbutton);
        feedback = findViewById(R.id.user_feedback);
        submit = findViewById(R.id.submit_feed);
        text.setText("Feedback And Suggestion");
        db = FirebaseFirestore.getInstance();

        dataBase = new com.codeIncubnator.boltfax.MyDataBase(this);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent abc = new Intent(Feedack_And_Suggestion.this, MainActivity.class);
                abc.putExtra("frag", "4");
                startActivity(abc);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dataBase.getAllInfo();
                if (cursor.getCount() != 0) {

                    while (cursor.moveToNext()) {

                        agh.add(cursor.getString(0));
                        agh.add(cursor.getString(1));
                        agh.add(cursor.getString(2));

                    }
                    d = new ProgressDialog(Feedack_And_Suggestion.this);
                    d.setMessage("Submitting...");
                    d.setCancelable(false);
                    d.show();

                    Map<String, Object> city = new HashMap<>();
                    city.put("feedback", feedback.getText().toString());
                    db.collection("feedbacks")
                            .add(city)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    d.dismiss();
                                    Toast.makeText(Feedack_And_Suggestion.this, "Thanks For Giving your Valuale feedback", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Feedack_And_Suggestion.this, MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            d.dismiss();
                            Toast.makeText(Feedack_And_Suggestion.this, "Thanks For Giving your Valuale feedback", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Feedack_And_Suggestion.this, MainActivity.class));

                        }
                    });

                }

            }
        });
    }
}
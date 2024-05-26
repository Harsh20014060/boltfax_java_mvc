package com.codeIncubnator.boltfax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TermAndConditions extends AppCompatActivity {

    WebView termsandcondition;
    TextView text;
    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_conditions);

        text = findViewById(R.id.universal_backstrip_text);
        backbutton = findViewById(R.id.universal_backstrip_backbutton);
        termsandcondition = findViewById(R.id.termsandconditionshowcase);

        termsandcondition.loadUrl("https://sundew.000webhostapp.com/termsandconditions.html");
        text.setText("Terms And Conditions");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent abc = new Intent(TermAndConditions.this, MainActivity.class);
                abc.putExtra("frag", "4");
                startActivity(abc);

            }
        });
    }
}
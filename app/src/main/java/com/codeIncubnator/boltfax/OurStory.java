package com.codeIncubnator.boltfax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OurStory extends AppCompatActivity {

    WebView ourstory;
    TextView text;
    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_story);

        text = findViewById(R.id.universal_backstrip_text);
        backbutton = findViewById(R.id.universal_backstrip_backbutton);
        ourstory = findViewById(R.id.ourstoryshowcase);

        ourstory.loadUrl("https://sundew.000webhostapp.com/about_us.html");

        text.setText("Our Story");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent abc = new Intent(OurStory.this, MainActivity.class);
                abc.putExtra("frag", "4");
                startActivity(abc);

            }
        });
    }
}
package com.codeIncubnator.boltfax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PrivacyAndPolicy extends AppCompatActivity {

    WebView privacypolicy;
    TextView text;
    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_policy);

        text = findViewById(R.id.universal_backstrip_text);
        backbutton = findViewById(R.id.universal_backstrip_backbutton);
        privacypolicy = findViewById(R.id.privacypolicyshowcase);


        privacypolicy.loadUrl("https://sundew.000webhostapp.com/privacy.html");
        text.setText("Privacy And Policy");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent redirection = getIntent();


                    Intent abc = new Intent(PrivacyAndPolicy.this, SignUpPage.class);
                    abc.putExtra("name", redirection.getStringExtra("name"));
                    abc.putExtra("contactno", redirection.getStringExtra("contactno"));
                    startActivity(abc);


                } catch (Exception e) {
                    Intent abc = new Intent(PrivacyAndPolicy.this, MainActivity.class);
                    abc.putExtra("frag", "4");
                    startActivity(abc);
                }


            }
        });
    }
}
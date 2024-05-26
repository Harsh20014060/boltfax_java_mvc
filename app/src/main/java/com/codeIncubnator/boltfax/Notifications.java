package com.codeIncubnator.boltfax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Notifications extends AppCompatActivity {

    TextView text;
    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        text = findViewById(R.id.universal_backstrip_text);
        backbutton = findViewById(R.id.universal_backstrip_backbutton);
        text.setText("Notifications");
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Notifications.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

}
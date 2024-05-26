package com.application.boltfax;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    VideoView view;
    com.application.boltfax.MyDataBase dataBase;
    FirebaseFirestore db;
    ArrayList<ArrayList<String>> dealslist = new ArrayList<ArrayList<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        view = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashscreen);
        view.setVideoURI(uri);
        view.start();

        db = FirebaseFirestore.getInstance();

        dataBase = new com.application.boltfax.MyDataBase(SplashScreen.this);
        view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Cursor cursor = dataBase.getAllInfo();
                if (cursor.getCount() == 0) {
                    Intent abc = new Intent(SplashScreen.this, com.application.boltfax.LoginPage.class);
                    startActivity(abc);
                } else {
                    Intent abc = new Intent(SplashScreen.this, com.application.boltfax.MainActivity.class);
                    startActivity(abc);
                }
            }
        });

    }
}
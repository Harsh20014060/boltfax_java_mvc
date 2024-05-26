package com.application.boltfax;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.application.boltfax.fragments.CartFragement;
import com.application.boltfax.fragments.CategoryFragment;
import com.application.boltfax.fragments.HomeFragment;
import com.application.boltfax.fragments.MenuFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomnavigation);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BoltFax");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.category));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.cart));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.menu));


        try {
            Intent take_val = getIntent();
            String fragment = String.valueOf(take_val.getStringExtra("frag"));

            int c = Integer.parseInt(fragment);
            bottomNavigation.show(c, true);
            replace(new MenuFragment());

        } catch (Exception ignored) {
            bottomNavigation.show(1, true);
            replace(new HomeFragment());

        }


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        replace(new HomeFragment());
                        break;
                    case 2:
                        replace(new CategoryFragment());

                        break;

                    case 3:
                        replace(new CartFragement());
                        break;

                    case 4:
                        replace(new MenuFragment());
                        break;

                    default:


                        Toast.makeText(MainActivity.this, "" + model, Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });


    }

    public void replace(Fragment frag) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.framelayout, frag);
        trans.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.search) {
            Intent search = new Intent(this, com.application.boltfax.SearchPage.class);
            startActivity(search);
        }
        if (i == R.id.notification) {
            Intent notify = new Intent(this, com.application.boltfax.Notifications.class);
            startActivity(notify);
        }
        return super.onOptionsItemSelected(item);
    }
}
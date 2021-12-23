package com.example.class2demo2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.class2demo2.databinding.ActivityMainDrawer2Binding;
import com.google.android.material.navigation.NavigationView;

public class MainDrawerActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainDrawer2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainDrawer2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainDrawer.toolbar);
        //FLOATING ICONS IMPL
        /*FloatingActionButton addFBtn = binding.appBarMainDrawer.mainAddFloatbtn;
        addFBtn.setOnClickListener(v -> {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main_drawer).navigate(R.id.action_studentListRvFragment_to_addFragment);
    });

        binding.appBarMainDrawer.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                  }
                });
        */
                DrawerLayout drawer = binding.drawerLayout;
                NavigationView navigationView = binding.navView;
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.studentListRvFragment, R.id.addFragment, R.id.nav_slideshow)
                        .setOpenableLayout(drawer)
                        .build();
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_drawer);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);




            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main_drawer, menu);
                return true;
            }

            @Override
            public boolean onSupportNavigateUp() {
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_drawer);
                return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                        || super.onSupportNavigateUp();
    }


    @Override
            public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                if (!super.onOptionsItemSelected(item)) {
                    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_drawer);
                    switch (item.getItemId()) {
                        case android.R.id.home:
                            NavigationUI.navigateUp(navController, mAppBarConfiguration);
                            return true;
                        default:
                            NavigationUI.onNavDestinationSelected(item, navController);
                    }
                } else {
                    return true;
                }
                return false;
            }

}
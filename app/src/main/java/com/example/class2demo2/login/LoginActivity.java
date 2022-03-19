package com.example.class2demo2.login;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import com.example.class2demo2.R;

public class LoginActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.login_navhost);
        navController = navHost.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    navController.navigateUp();
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
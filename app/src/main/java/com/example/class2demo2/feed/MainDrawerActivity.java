package com.example.class2demo2.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.class2demo2.R;
import com.example.class2demo2.databinding.ActivityMainDrawer2Binding;
import com.example.class2demo2.databinding.NavHeaderMainDrawerBinding;
import com.example.class2demo2.feed.MemberList.MemberListRvFragment;
import com.example.class2demo2.feed.MemberList.MemberListRvViewModel;
import com.example.class2demo2.login.LoginActivity;
import com.example.class2demo2.model.AppLocalDb;
import com.example.class2demo2.model.Member;
import com.example.class2demo2.model.MemberDao_Impl;
import com.example.class2demo2.model.MemberViewModel;
import com.example.class2demo2.model.Model;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

public class MainDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainDrawer2Binding binding;
    TextView curNameTv;
    TextView curMailTv;
    ImageView curImage;
    Member m;
    MemberViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainDrawer2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainDrawer.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        viewModel = new ViewModelProvider(this).get(MemberViewModel.class);

        //logout click
        navigationView.getMenu().findItem(R.id.loginFragment).setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.loginFragment) {
                Model.instance.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.memberListRvFragment, R.id.addFragment, R.id.addPostFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        viewModel.getData().observe(this, members -> {
            View header = navigationView.getHeaderView(0);
            curNameTv = (TextView) header.findViewById(R.id.navheader_name_tv);
            curMailTv = (TextView) header.findViewById(R.id.navheader_mail_tv);
            curImage = (ImageView) header.findViewById(R.id.navheader_image_iv);
            String uid = Model.instance.getUid();
            for(Member m: members){
                if(uid.equals(m.getId())) {
                    curNameTv.setText(m.getName());
                    curMailTv.setText(m.getAddress());
                    if(m.getAvatar()!=null) {
                        Picasso.get()
                                .load(m.getAvatar())
                                .into(curImage);
                    }
                }
            }
        });


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
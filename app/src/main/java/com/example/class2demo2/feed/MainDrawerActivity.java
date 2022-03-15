package com.example.class2demo2.feed;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.class2demo2.NavGraphDirections;
import com.example.class2demo2.R;
import com.example.class2demo2.databinding.ActivityMainDrawer2Binding;
import com.example.class2demo2.databinding.NavHeaderMainDrawerBinding;
import com.example.class2demo2.feed.MemberList.MemberListRvFragment;
import com.example.class2demo2.feed.MemberList.MemberListRvFragmentDirections;
import com.example.class2demo2.feed.MemberList.MemberListRvViewModel;
import com.example.class2demo2.login.LoginActivity;
import com.example.class2demo2.model.AppLocalDb;
import com.example.class2demo2.model.Member;
import com.example.class2demo2.model.MemberDao;
//import com.example.class2demo2.model.MemberDao_Impl;
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
    MemberViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainDrawer2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainDrawer.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        getWindow().setStatusBarColor(Color.TRANSPARENT);
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
                R.id.postListRvFragment, R.id.memberListRvFragment, R.id.addPostFragment, R.id.addCategoryFragment, R.id.categoryListRvFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //set appbar header with current user details
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
                    header.setOnClickListener(v -> {
                        navController.navigate(NavGraphDirections.actionGlobalMemberDetailsFragment(uid,uid));
                        drawer.closeDrawers();
                    });
                    if(m.getAvatar()!=null) {
                        Picasso.get()
                                .load(m.getAvatar())
                                .into(curImage);
                    }
                    //hide add category from user
                    if(m.getUserType().equals(Member.UserType.USER.toString()))
                        navigationView.getMenu().findItem(R.id.addCategoryFragment).setVisible(false);
                }
            }
        });

        //My Posts click
        navigationView.getMenu().findItem(R.id.myPostsList).setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.myPostsList) {
                NavGraphDirections.ActionGlobalUserPostListRvFragment action = NavGraphDirections.actionGlobalUserPostListRvFragment(Model.instance.getUid());
                navController.navigate(action);
                drawer.closeDrawers();
            }
            return true;
        });
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
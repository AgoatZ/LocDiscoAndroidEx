package com.example.class2demo2.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.class2demo2.R;
import com.example.class2demo2.feed.MainDrawerActivity;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginBtn = view.findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(v ->{
            //TODO - CONNECT TO MODEL LOGIN FUNCTION
            toFeedActivity();
        });
        return view;
    }

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), MainDrawerActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
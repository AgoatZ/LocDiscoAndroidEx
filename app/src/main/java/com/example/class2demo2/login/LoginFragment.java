package com.example.class2demo2.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.class2demo2.R;
import com.example.class2demo2.feed.MainDrawerActivity;
import com.example.class2demo2.model.Model;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginBtn = view.findViewById(R.id.login_login_btn);
        TextInputEditText emailTil = view.findViewById(R.id.login_email_et);
        TextInputEditText passwordTil = view.findViewById(R.id.login_password_et);
        loginBtn.setOnClickListener(v ->{
            //TODO - CONNECT TO MODEL LOGIN FUNCTION
            /*
            Model.instance.signIn(emailTil.getEditText().getText().toString(),
                    passwordTil.getEditText().getText().toString(),
                    user -> toFeedActivity()
                );
            */
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
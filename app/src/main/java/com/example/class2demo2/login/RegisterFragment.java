package com.example.class2demo2.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.class2demo2.R;
import com.example.class2demo2.feed.MainDrawerActivity;
import com.example.class2demo2.model.Member;
import com.example.class2demo2.model.Model;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button registerBtn = view.findViewById(R.id.register_register_btn);
        TextInputEditText emailTil = view.findViewById(R.id.register_email_et);
        TextInputEditText passwordTil = view.findViewById(R.id.register_password_et);
        TextView loginTv = view.findViewById(R.id.register_login_tv);

        loginTv.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(RegisterFragmentDirections.actionGlobalLoginFragment());
        });

        registerBtn.setOnClickListener(v ->{
            Model.instance.register(emailTil.getEditableText().toString(),
                    passwordTil.getEditableText().toString(),
                    (user,error) -> {
                    if(user!=null)
                        Model.instance.addMember(new Member
                                        (user.getDisplayName(),
                                         user.getUid(),
                                        null,
                                        user.getEmail(),
                                        false,
                                        null),
                                () -> toFeedActivity());
                    else
                        Toast.makeText(this.getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            });
        });
        return view;
    }

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), MainDrawerActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
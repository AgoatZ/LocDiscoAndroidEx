package com.example.class2demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters


    public AddFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void save() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        Student student = new Student(nameTv.getText().toString(), idTv.getText().toString(), phoneTv.getText().toString(), addressTv.getText().toString(), cb.isChecked(), avatar.getId());
        Model.instance.addStudent(student, () -> {
            Navigation.findNavController(nameTv).navigateUp();
        });
    }

        EditText nameTv;
        EditText idTv;
        EditText phoneTv;
        EditText addressTv;
        Button cancelBtn;
        Button saveBtn;
        CheckBox cb;
        ImageView avatar;
        ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        progressBar = view.findViewById(R.id.add_progressbar);
        progressBar.setVisibility(View.GONE);
        nameTv = view.findViewById(R.id.add_name_txt);
        idTv = view.findViewById(R.id.add_id_txt);
        phoneTv = view.findViewById(R.id.add_phone_txt);
        addressTv = view.findViewById(R.id.add_address_txt);
        cb = view.findViewById(R.id.add_checked_chk);
        avatar = view.findViewById(R.id.add_student_imgv);
        cancelBtn = view.findViewById(R.id.add_cancel_btn);
        saveBtn = view.findViewById(R.id.add_save_btn);
        cancelBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        saveBtn.setOnClickListener(v -> {
            save();
            });

        return view;
    }
}
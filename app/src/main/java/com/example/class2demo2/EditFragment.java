package com.example.class2demo2;

import android.content.Intent;
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

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STUDENT_ID = "ARG_STUDENT_ID";

    // TODO: Rename and change types of parameters
    private String studentId;

    public EditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String studentId) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STUDENT_ID, studentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentId = getArguments().getString(ARG_STUDENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        studentId = StudentDetailsFragmentArgs.fromBundle(getArguments()).getStudentId();
        Student student = Model.instance.getStudentById(studentId);

        EditText name;
        EditText id;
        EditText phone;
        EditText address;
        CheckBox checked;
        ImageView avatar;
        Button cancelBtn;
        Button saveBtn;
        Button deleteBtn;

        name = view.findViewById(R.id.edit_name_txt);
        id = view.findViewById(R.id.edit_id_txt);
        phone = view.findViewById(R.id.edit_phone_txt);
        address = view.findViewById(R.id.edit_address_txt);
        checked = view.findViewById(R.id.edit_checked_chk);
        avatar = view.findViewById(R.id.edit_student_imgv);
        saveBtn = view.findViewById(R.id.edit_save_btn);
        cancelBtn = view.findViewById(R.id.edit_cancel_btn);
        deleteBtn = view.findViewById(R.id.edit_delete_btn);

        name.setText(student.getName());
        id.setText(student.getId());
        phone.setText(student.getPhone());
        address.setText(student.getAddress());
        checked.setChecked(student.isFlag());


        saveBtn.setOnClickListener(v -> {

            student.setAddress(address.getText().toString());
            student.setId(id.getText().toString());
            student.setAvatar(avatar.getId());
            student.setFlag(checked.isChecked());
            student.setName(name.getText().toString());
            student.setPhone(phone.getText().toString());
            Navigation.findNavController(v).navigateUp();
        });
        cancelBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        deleteBtn.setOnClickListener(v -> {
            Model.instance.getAllStudents().remove(student);
            Navigation.findNavController(v).navigate(EditFragmentDirections.actionEditFragmentToStudentListRvFragment());
        });
        return view;
    }
}
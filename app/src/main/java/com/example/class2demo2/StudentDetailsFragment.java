package com.example.class2demo2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STUDENT_ID = "ARG_STUDENT_ID";

    // TODO: Rename and change types of parameters
    private String studentId;


    public StudentDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StudentDetailsFragment newInstance(String studentId) {
        StudentDetailsFragment fragment = new StudentDetailsFragment();
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

    Student student;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_details, container, false);

        studentId = StudentDetailsFragmentArgs.fromBundle(getArguments()).getStudentId();
        Model.instance.getStudentById(studentId,(s->{
            student = s;
            TextView nameTv = view.findViewById(R.id.details_name_txt);
            TextView idTv = view.findViewById(R.id.details_id_txt);
            TextView phoneTv = view.findViewById(R.id.details_phone_txt);
            TextView addressTv = view.findViewById(R.id.details_address_txt);
            CheckBox cb = view.findViewById(R.id.details_checked_chk);
            Button editBtn = view.findViewById(R.id.details_to_edit_btn);
            ImageView avatar = view.findViewById(R.id.details_student_imgv);


            nameTv.setText(student.getName());
            idTv.setText(student.getId());
            addressTv.setText(student.getAddress());
            phoneTv.setText(student.getPhone());
            cb.setChecked(student.isFlag());
        }));


        Button backBtn = view.findViewById(R.id.details_to_edit_btn);
        backBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(StudentDetailsFragmentDirections.actionStudentDetailsFragmentToEditFragment(studentId));
        });

        return view;
    }
}
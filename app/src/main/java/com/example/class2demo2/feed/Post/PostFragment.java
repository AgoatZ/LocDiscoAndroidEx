package com.example.class2demo2.feed.Post;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.class2demo2.R;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POST_ID = "ARG_POST_ID";

    // TODO: Rename and change types of parameters
    private String postId;


    public PostFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String postId) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getString(ARG_POST_ID);
        }
    }

    PostViewModel viewModel;
    Student student;
    TextView nameTv;
    TextView areaTv;
    TextView categoryTv;
    TextView addressTv;
    TextView descriptionTv;
    CheckBox cb;
    Button editBtn;
    ImageView image;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //viewModel = new ViewModelProvider(this).get(PostViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        Model.instance.getStudentsListLoadingState().postValue(Model.StudentsListLoadingState.loading);
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();
        //student = viewModel.getData(studentId).getValue();

        nameTv = view.findViewById(R.id.post_name_txt);
        areaTv = view.findViewById(R.id.post_area_txt);
        addressTv = view.findViewById(R.id.post_address_txt);
        categoryTv = view.findViewById(R.id.post_category_txt);
        nameTv = view.findViewById(R.id.post_name_txt);
        addressTv = view.findViewById(R.id.post_address_txt);
        image = view.findViewById(R.id.post_student_imgv);
        editBtn = view.findViewById(R.id.post_to_edit_btn);
        descriptionTv = view.findViewById(R.id.post_description_txt);

        if(student != null) {
            nameTv.setText(student.getName());
            areaTv.setText(student.getId());
            addressTv.setText(student.getAddress());
            categoryTv.setText(student.getPhone());
            if (student.getAvatar() != null) {
                Picasso.get()
                        .load(student.getAvatar())
                        .into(image);
            }
        }
        /***********************************/
        /*
        editBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(PostFragmentDirections.actionPostFragmentToEditFragment(studentId));
        });
        viewModel.getData(studentId).observe(getViewLifecycleOwner(), student1 -> {});
*/
        return view;


    }
}
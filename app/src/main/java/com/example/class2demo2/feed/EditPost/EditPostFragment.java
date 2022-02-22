package com.example.class2demo2.feed.EditPost;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.class2demo2.R;
import com.example.class2demo2.feed.Edit.EditViewModel;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POST_ID = "ARG_POST_ID";
    private static final String ARG_MEMBER_ID = "ARG_MEMBER_ID";

    // TODO: Rename and change types of parameters
    private String postId;
    private String memberId;

    public EditPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param postId Parameter 1.
     * @param memberId Parameter 2.
     * @return A new instance of fragment EditPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPostFragment newInstance(String postId, String memberId) {
        EditPostFragment fragment = new EditPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postId);
        args.putString(ARG_MEMBER_ID, memberId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getString(ARG_POST_ID);
            memberId=getArguments().getString(ARG_MEMBER_ID);
        }

    }

    public void save() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        progressBar.setVisibility(View.GONE);

        String snameTv = nameTv.getText().toString();
        String scategoryTv = categoryTv.getText().toString();
        String sareaTv = areaTv.getText().toString();
        String saddressTv = addressTv.getText().toString();
        String sdescriptionTv = descriptionTv.getText().toString();

        Post post = new Post(snameTv, UUID.randomUUID().toString(), scategoryTv, saddressTv, null, sareaTv, Model.instance.getUid(), sdescriptionTv);
        if (imageBitmap != null){
            Model.instance.saveImage(imageBitmap, "P" + post.getId() + "U"+ post.getUserId() + ".jpg", url -> {
                post.setImage(url);
                Model.instance.addPost(post, () -> {
                    Navigation.findNavController(nameTv).navigateUp();
                });
            });
        }else{
            Model.instance.addPost(post, () -> {
                Navigation.findNavController(nameTv).navigateUp();
            });
        }
    }
    //TODO I'VE STOPPED HERE
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_OPEN = 2;
    Bitmap imageBitmap;
    EditText nameTv;
    AutoCompleteTextView categoryTv;
    EditText areaTv;
    EditText addressTv;
    EditText descriptionTv;
    Button cancelBtn;
    Button saveBtn;
    ImageView image;
    ImageButton cameraBtn;
    ImageButton galleryBtn;
    ProgressBar progressBar;
    EditPostViewModel viewModel;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(EditPostViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        String postId= EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
        String memberId= EditPostFragmentArgs.fromBundle(getArguments()).getMemberId();
        Post post =viewModel.getData(postId.toString()).getValue();


        progressBar = view.findViewById(R.id.edit_post_progressbar);
        progressBar.setVisibility(View.GONE);
        nameTv = view.findViewById(R.id.edit_post_name_txt);
        categoryTv = view.findViewById(R.id.edit_post_category_txt);
        areaTv = view.findViewById(R.id.edit_post_area_txt);
        addressTv = view.findViewById(R.id.edit_post_address_txt);
        descriptionTv = view.findViewById(R.id.edit_post_description_txt);
        image = view.findViewById(R.id.edit_post_imgv);
        cancelBtn = view.findViewById(R.id.edit_post_cancel_btn);
        saveBtn = view.findViewById(R.id.edit_post_save_btn);
        cameraBtn = view.findViewById(R.id.edit_post_camera_btn);
        galleryBtn = view.findViewById(R.id.edit_post_gallery_btn);


        nameTv.setText(post.getName());
        categoryTv.setText(post.getCategory());
        areaTv.setText(post.getArea());
        addressTv.setText(post.getAddress());
        descriptionTv.setText(post.getDescription());
        //image.setImageBitmap(post.getImage().);



        cancelBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        saveBtn.setOnClickListener(v -> {
            save();
        });
        cameraBtn.setOnClickListener(v -> {
            openCamera();
        });
        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });

        List<String> items = new ArrayList<String>();
        items.add("Option 1");
        ArrayAdapter<String> adapter = new ArrayAdapter(requireContext(), R.layout.category_list_item, items);
        categoryTv.setAdapter(adapter);



        return view;
    }

    private void openGallery() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_OPEN);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                image.setImageBitmap(imageBitmap);
            }
        }
        else if(requestCode == REQUEST_GALLERY_OPEN){
            if(resultCode == RESULT_OK){
                try{
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    image.setImageBitmap(imageBitmap);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to select image from gallery", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
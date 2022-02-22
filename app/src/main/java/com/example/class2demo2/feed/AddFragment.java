package com.example.class2demo2.feed;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.class2demo2.R;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Member;

import java.io.InputStream;

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
        Member member = new Member(nameTv.getText().toString(), idTv.getText().toString(), phoneTv.getText().toString(), addressTv.getText().toString(), cb.isChecked(), null);
        if (imageBitmap != null){
            Model.instance.saveImage(imageBitmap, idTv.getText() + ".jpg", url -> {
                member.setAvatar(url);
                Model.instance.addMember(member, () -> {
                    Navigation.findNavController(nameTv).navigateUp();
                });
            });
        }else{
            Model.instance.addMember(member, () -> {
                Navigation.findNavController(nameTv).navigateUp();
            });
        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_OPEN = 2;
    Bitmap imageBitmap;
    EditText nameTv;
    EditText idTv;
    EditText phoneTv;
    EditText addressTv;
    Button cancelBtn;
    Button saveBtn;
    CheckBox cb;
    ImageView avatar;
    ImageButton cameraBtn;
    ImageButton galleryBtn;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        progressBar = view.findViewById(R.id.edit_progressbar);
        progressBar.setVisibility(View.GONE);
        nameTv = view.findViewById(R.id.add_name_txt);
        idTv = view.findViewById(R.id.add_id_txt);
        phoneTv = view.findViewById(R.id.add_phone_txt);
        addressTv = view.findViewById(R.id.add_address_txt);
        cb = view.findViewById(R.id.add_checked_chk);
        avatar = view.findViewById(R.id.add_member_imgv);
        cancelBtn = view.findViewById(R.id.add_cancel_btn);
        saveBtn = view.findViewById(R.id.add_save_btn);
        cameraBtn = view.findViewById(R.id.edit_camera_btn);
        galleryBtn = view.findViewById(R.id.edit_gallery_btn);

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
                avatar.setImageBitmap(imageBitmap);
            }
        }
        else if(requestCode == REQUEST_GALLERY_OPEN){
            if(resultCode == RESULT_OK){
                try{
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    avatar.setImageBitmap(imageBitmap);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to select image from gallery", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
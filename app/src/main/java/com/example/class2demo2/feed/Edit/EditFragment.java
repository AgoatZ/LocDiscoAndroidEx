package com.example.class2demo2.feed.Edit;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.class2demo2.NavGraphDirections;
import com.example.class2demo2.R;
import com.example.class2demo2.feed.Details.MemberDetailsFragmentArgs;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Member;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MEMBER_ID = "ARG_MEMBER_ID";
    private static final String ARG_CURR_MEMBER_ID = "ARG_CURR_MEMBER_ID";


    // TODO: Rename and change types of parameters
    private String memberId;
    private String currMemberId;


    public EditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String memberId, String currMemberId) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEMBER_ID, memberId);
        args.putString(ARG_CURR_MEMBER_ID, currMemberId);
        fragment.setArguments(args);
        return fragment;
    }

    public void save() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        member.setAddress(address.getText().toString());
        member.setId(memberId);
        member.setAvatar(member.getAvatar());
        member.setName(name.getText().toString() + " " + lastName.getText().toString());
        member.setPhone(phone.getText().toString());
        if (imageBitmap != null) {
            Model.instance.saveImage(imageBitmap, memberId + ".jpg", url -> {
                member.setAvatar(url);
                Model.instance.addMember(member, () -> {
                    Navigation.findNavController(name).navigate(EditFragmentDirections.actionEditFragmentToMemberDetailsFragment(member.getId(), Model.instance.getUid()));
                });
            });
        } else {
            Model.instance.addMember(member, () -> {
                Navigation.findNavController(name).navigate(EditFragmentDirections.actionEditFragmentToMemberDetailsFragment(member.getId(), Model.instance.getUid()));
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            memberId = getArguments().getString(ARG_MEMBER_ID);
            currMemberId = getArguments().getString(ARG_CURR_MEMBER_ID);
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_OPEN = 2;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    EditViewModel viewModel;
    EditText name;
    EditText lastName;
    EditText phone;
    EditText address;
    ImageView avatar;
    Button cancelBtn;
    Button saveBtn;
    Button deleteBtn;
    ImageButton galleryBtn;
    ImageButton cameraBtn;
    Member member;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(EditViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        memberId = MemberDetailsFragmentArgs.fromBundle(getArguments()).getMemberId();
        currMemberId = MemberDetailsFragmentArgs.fromBundle(getArguments()).getCurrMemberId();

        name = view.findViewById(R.id.edit_name_txt);
        lastName = view.findViewById(R.id.edit_lastname_txt);
        phone = view.findViewById(R.id.edit_phone_txt);
        address = view.findViewById(R.id.edit_address_txt);
        avatar = view.findViewById(R.id.edit_member_imgv);
        saveBtn = view.findViewById(R.id.edit_save_btn);
        cancelBtn = view.findViewById(R.id.edit_cancel_btn);
        deleteBtn = view.findViewById(R.id.edit_delete_btn);
        progressBar = view.findViewById(R.id.edit_progressbar);
        progressBar.setVisibility(View.GONE);
        galleryBtn = view.findViewById(R.id.edit_gallery_btn);
        cameraBtn = view.findViewById(R.id.edit_camera_btn);

        viewModel.getData(memberId).observe(getViewLifecycleOwner(), member1 -> {
            member = member1;
            if (member == null) {
                Toast.makeText(this.getContext(), "This member does no longer exist", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(container).navigate(NavGraphDirections.actionGlobalMemberListRvFragment());
            } else {
                String[] strName = member.getName().split(" ");
                name.setText(strName[0]);
                lastName.setText(strName[1]);
                phone.setText(member.getPhone());
                address.setText(member.getAddress());
            }
        });
        saveBtn.setOnClickListener(v -> {
            save();
        });


        cancelBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });


        deleteBtn.setOnClickListener(v -> {
            Model.instance.logicalDelete(member, () -> {
                Navigation.findNavController(v).navigate(EditFragmentDirections.actionEditFragmentToMemberListRvFragment());
            });
        });

        cameraBtn = view.findViewById(R.id.edit_camera_btn);
        galleryBtn = view.findViewById(R.id.edit_gallery_btn);

        cancelBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
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
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                avatar.setImageBitmap(imageBitmap);
            }
        } else if (requestCode == REQUEST_GALLERY_OPEN) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    avatar.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to select image from gallery", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
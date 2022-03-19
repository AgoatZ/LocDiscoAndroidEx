package com.example.class2demo2.feed.Post;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.class2demo2.NavGraphDirections;
import com.example.class2demo2.R;
import com.example.class2demo2.model.Member;
import com.example.class2demo2.model.MemberViewModel;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    private static final String ARG_POST_ID = "ARG_POST_ID";
    private static final String ARG_POST_UID = "ARG_POST_UID";

    private String postId;
    private String postUId;


    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(String postId, String postUId) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postId);
        args.putString(ARG_POST_UID, postUId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getString(ARG_POST_ID);
            postUId = getArguments().getString(ARG_POST_UID);
        }
    }

    PostViewModel viewModel;
    MemberViewModel memberViewModel;
    Post post;
    TextView nameTv;
    TextView areaTv;
    TextView categoryTv;
    TextView addressTv;
    TextView descriptionTv;
    TextView postOwnerNameTv;
    Button editBtn;
    ImageView image;
    ImageView postOwnerImage;
    Member currMember;
    Member postOwner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        memberViewModel = new ViewModelProvider(requireActivity()).get(MemberViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        Model.instance.getPostsListLoadingState().postValue(Model.PostsListLoadingState.loading);
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();
        postUId = PostFragmentArgs.fromBundle(getArguments()).getPostUId();
        post = viewModel.getData(postId).getValue();
        Model.instance.refreshPostsList();

        //Set view components
        nameTv = view.findViewById(R.id.post_name_txt);
        areaTv = view.findViewById(R.id.post_area_txt);
        addressTv = view.findViewById(R.id.post_address_txt);
        categoryTv = view.findViewById(R.id.post_category_txt);
        nameTv = view.findViewById(R.id.post_name_txt);
        addressTv = view.findViewById(R.id.post_address_txt);
        image = view.findViewById(R.id.post_member_imgv);
        editBtn = view.findViewById(R.id.post_to_edit_btn);
        editBtn.setVisibility(View.VISIBLE);
        descriptionTv = view.findViewById(R.id.post_description_txt);
        postOwnerImage = view.findViewById(R.id.post_user_image_iv);
        postOwnerNameTv = view.findViewById(R.id.post_user_info_tv);

        viewModel.getData(postId).observe(getViewLifecycleOwner(), post1 -> {
            if (post == null) {
                post = post1;
            }

            //Set relevant data if post is not deleted
            Model.instance.isPostDeletedFromDb(post, isDeleted -> {
                if (isDeleted) {
                    Navigation.findNavController(nameTv).navigate(NavGraphDirections.actionGlobalPostListRvFragment());
                    Toast.makeText(getContext(), "This post does not exist anymore", Toast.LENGTH_LONG).show();
                } else {
                    if (post1 != null) {
                        post = post1;
                    }
                    nameTv.setText(post.getName());
                    areaTv.setText(post.getArea());
                    addressTv.setText(post.getAddress());
                    categoryTv.setText(post.getCategory());
                    descriptionTv.setText(post.getDescription());
                    if (post.getImage() != null) {
                        Picasso.get()
                                .load(post.getImage())
                                .into(image);
                    }
                }
            });
        });

        //Get users info and set view accordingly
        memberViewModel.getData().observe(getViewLifecycleOwner(), members -> {
            for (Member m : members) {
                if (m.getId().equals(postUId)) {
                    postOwner = m;
                }
                if (m.getId().equals(Model.instance.getUid())) {
                    currMember = m;
                }
            }
            if (postOwner != null) {
                Picasso.get().load(postOwner.getAvatar()).into(postOwnerImage);
                postOwnerNameTv.setText(postOwner.getName());
            } else {
                postOwnerNameTv.setText("Deleted Member");
            }

            if (!Model.instance.getUid().equals(postUId)
                    && !currMember
                    .getUserType()
                    .equals(Member
                            .UserType
                            .ADMIN
                            .toString())) {
                editBtn.setVisibility(View.GONE);
            }
        });

        Model.instance.refreshMembersList();

        editBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(PostFragmentDirections.actionGlobalEditPostFragment(postId, postUId));
        });

        postOwnerNameTv.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(PostFragmentDirections.actionPostFragmentToMemberDetailsFragment(postUId, Model.instance.getUid()));
        });

        return view;
    }
}
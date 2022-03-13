package com.example.class2demo2.feed.Details;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.class2demo2.NavGraphDirections;
import com.example.class2demo2.R;
import com.example.class2demo2.model.MemberViewModel;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Member;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MEMBER_ID = "ARG_MEMBER_ID";
    private static final String ARG_CURR_MEMBER_ID = "ARG_CURR_MEMBER_ID";

    // TODO: Rename and change types of parameters
    private String memberId;
    private String currMemberId;


    public MemberDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MemberDetailsFragment newInstance(String memberId, String currMemberId) {
        MemberDetailsFragment fragment = new MemberDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEMBER_ID, memberId);
        args.putString(ARG_CURR_MEMBER_ID, currMemberId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            memberId = getArguments().getString(ARG_MEMBER_ID);
            currMemberId = getArguments().getString(ARG_CURR_MEMBER_ID);
        }
    }

    MemberDetailsViewModel viewModel;
    MemberViewModel memberViewModel;
    Member member;
    Member currMember;
    TextView nameTv;
    TextView idTv;
    TextView phoneTv;
    TextView addressTv;
    Button editBtn;
    Button postsBtn;
    ImageView avatar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(MemberDetailsViewModel.class);
        memberViewModel = new ViewModelProvider(requireActivity()).get(MemberViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        Model.instance.getMembersListLoadingState().postValue(Model.MembersListLoadingState.loading);
        View view = inflater.inflate(R.layout.fragment_member_details, container, false);
        Model.instance.refreshMembersList();

        //GET RELEVANT DATA FROM DB
        memberId = MemberDetailsFragmentArgs.fromBundle(getArguments()).getMemberId();
        currMemberId = MemberDetailsFragmentArgs.fromBundle(getArguments()).getCurrMemberId();
        member = viewModel.getData(memberId).getValue();

        /*
        viewModel.getData(memberId).observe(getViewLifecycleOwner(), member1 -> {
            member = member1;
            if(member == null) {
                Toast.makeText(this.getContext(), "This member does no longer exist", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(container).navigate(NavGraphDirections.actionGlobalMemberListRvFragment());
            }
        });

         */
        //SET VIEW COMPONENTS
        nameTv = view.findViewById(R.id.details_name_txt);
        idTv = view.findViewById(R.id.details_id_txt);
        phoneTv = view.findViewById(R.id.details_phone_txt);
        addressTv = view.findViewById(R.id.details_address_txt);
        editBtn = view.findViewById(R.id.details_to_edit_btn);
        postsBtn = view.findViewById(R.id.details_user_post_list_btn);

        //memberViewModel.getData().observe(getViewLifecycleOwner(), members ->
        List<Member> members = memberViewModel.getData().getValue();
        {
            for(Member m :members) {
                if(m.getId().equals(currMemberId)) {
                    currMember = m;
                }
                if(m.getId().equals(memberId)) {
                    member = m;
                }
            }
            if (member == null) {
                Toast.makeText(this.getContext(), "This member does no longer exist", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(container).navigate(NavGraphDirections.actionGlobalMemberListRvFragment());
            } else {
                if ((!currMemberId.equals(memberId))
                        && !(currMember
                        .getUserType()
                        .equals(Member
                                .UserType
                                .ADMIN
                                .toString()))) {
                    editBtn.setVisibility(View.GONE);
                }
                avatar = view.findViewById(R.id.details_member_imgv);
                //SET DATA TO DISPLAY FROM DB
                nameTv.setText(member.getName());
                idTv.setText(member.getId());
                addressTv.setText(member.getAddress());
                phoneTv.setText(member.getPhone());
                if (member.getAvatar() != null) {
                    Picasso.get()
                            .load(member.getAvatar())
                            .into(avatar);
                }
            }
        }//);
                /***********************************/
                editBtn.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(MemberDetailsFragmentDirections.actionMemberDetailsFragmentToEditFragment(memberId, Model.instance.getUid()));
                });

                postsBtn.setOnClickListener(v -> {
                    NavGraphDirections.ActionGlobalUserPostListRvFragment action = NavGraphDirections.actionGlobalUserPostListRvFragment(memberId);
                    Navigation.findNavController(v).navigate(action);
                });

        Model.instance.refreshMembersList();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Model.instance.refreshMembersList();
    }
}
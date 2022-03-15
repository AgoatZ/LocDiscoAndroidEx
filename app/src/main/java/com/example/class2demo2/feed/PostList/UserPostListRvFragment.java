package com.example.class2demo2.feed.PostList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.class2demo2.R;
import com.example.class2demo2.feed.Edit.EditFragment;
import com.example.class2demo2.model.MemberViewModel;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;
import com.example.class2demo2.model.Member;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UserPostListRvFragment extends Fragment {

    private static final String ARG_USER_ID = "ARG_USER_ID";

    private String userId;

    public UserPostListRvFragment() {
        // Required empty public constructor
    }

    public static UserPostListRvFragment newInstance(String userId) {
        UserPostListRvFragment fragment = new UserPostListRvFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    //MEMBERS
    PostListViewModel viewModel;
    MemberViewModel memberViewModel;
    RecyclerView listRv;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    Member postsOwner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostListViewModel.class);
        memberViewModel = new ViewModelProvider(requireActivity()).get(MemberViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_post_list_rv, container, false);
        userId = UserPostListRvFragmentArgs.fromBundle(getArguments()).getUserId();

        memberViewModel.getData().observe(getViewLifecycleOwner(), members -> {
            for (Member m : members) {
                if (m.getId().equals(userId)) {
                    postsOwner = m;
                }
            }
            if (!userId.equals("")) {
                if (postsOwner != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(postsOwner.getName());
                } else {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Deleted Member");
                }
            }
        });

        //setting the recycler view
        swipeRefresh = view.findViewById(R.id.user_postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> {
            Model.instance.refreshPostsList();
            Model.instance.refreshPostsByMember(userId);
        });

        listRv = view.findViewById(R.id.user_postlist_rv);
        listRv.setHasFixedSize(true);
        listRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        listRv.setAdapter(adapter);

        //setting the adapter listeners
        adapter.setOnItemClickListener((v, position) -> {
                String postId = viewModel.getDataByMember(userId).getValue().get(position).getId();
                String postUId = viewModel.getDataByMember(userId).getValue().get(position).getUserId();
                Navigation.findNavController(v).navigate(UserPostListRvFragmentDirections.actionGlobalPostFragment(postId, postUId));
        });
        viewModel.getDataByMember(userId).observe(getViewLifecycleOwner(), list -> refresh());

        swipeRefresh.setRefreshing(Model.instance.getPostsListLoadingState().getValue() == Model.PostsListLoadingState.loading);
        Model.instance.getPostsListLoadingState().observe(getViewLifecycleOwner(), postsListLoadingState -> {
            swipeRefresh.setRefreshing(Model.instance.getPostsListLoadingState().getValue() == Model.PostsListLoadingState.loading);
        });
        Model.instance.refreshPostsList();
        Model.instance.refreshPostsByMember(userId);
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }


    //HOLDER CLASS
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView categoryTv;
        TextView areaTv;
        ImageView image;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.postrow_name_tv);
            categoryTv = itemView.findViewById(R.id.postrow_category_tv);
            image = itemView.findViewById(R.id.postrow_image_imv);
            areaTv = itemView.findViewById(R.id.postrow_area_tv);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(itemView, pos);
            });
        }

        public void bind(Post post) {
            if (post != null) {
                nameTv.setText(post.getName());
                categoryTv.setText(post.getCategory());
                areaTv.setText(post.getAddress());
                image.setImageResource(R.drawable.avatarsmith);
                if (post.getImage() != null) {
                    Picasso.get()
                            .load(post.getImage())
                            .into(image);
                }
            }
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    //ADAPTER CLASS
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = viewModel.getDataByMember(userId).getValue().get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getDataByMember(userId).getValue() == null) {
                return 0;
            }
            return viewModel.getDataByMember(userId).getValue().size();
        }
    }
}
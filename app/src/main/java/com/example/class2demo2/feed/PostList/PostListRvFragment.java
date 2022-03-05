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
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;
import com.example.class2demo2.model.Member;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PostListRvFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME";
    private static final String ARG_USER_ID = "ARG_USER_ID";

    private String categoryName;
    private String userId;


    public PostListRvFragment() {
        // Required empty public constructor
    }

    public static PostListRvFragment newInstance(String categoryName, String userId) {
        PostListRvFragment fragment = new PostListRvFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    //MEMBERS
    PostListViewModel viewModel;
    RecyclerView listRv;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list_rv,container,false);
        categoryName = PostListRvFragmentArgs.fromBundle(getArguments()).getCategoryName();
        userId = PostListRvFragmentArgs.fromBundle(getArguments()).getUserId();

        if(!userId.equals("")) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Model.instance.getMemberById(userId).getValue().getName());
        }
        if(!categoryName.equals("")) {
            Log.d("CatTitle: ", categoryName);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(categoryName);
        }
        //setting the recycler view
        swipeRefresh = view.findViewById(R.id.postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() ->{
            Model.instance.refreshPostsList();
        });

        listRv = view.findViewById(R.id.postlist_rv);
        listRv.setHasFixedSize(true);
        listRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        listRv.setAdapter(adapter);

        //setting the adapter listeners
        adapter.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, int position) {
                String postId = viewModel.getData().getValue().get(position).getId();
                String postUId=viewModel.getData().getValue().get(position).getUserId();
                Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionGlobalPostFragment(postId,postUId));
            }

            @Override
            public void onImageClick(View v, int position) {
                String postId = viewModel.getData().getValue().get(position).getId();
                String postUId = viewModel.getData().getValue().get(position).getUserId();
                Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionGlobalPostFragment(postId,postUId));
            }
        });
        viewModel.getData().observe(getViewLifecycleOwner(), list -> refresh());

        swipeRefresh.setRefreshing(Model.instance.getPostsListLoadingState().getValue() == Model.PostsListLoadingState.loading);
        Model.instance.getPostsListLoadingState().observe(getViewLifecycleOwner(), postsListLoadingState -> {
            swipeRefresh.setRefreshing(Model.instance.getPostsListLoadingState().getValue() == Model.PostsListLoadingState.loading);
        });
        Model.instance.refreshPostsList();
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }


    //HOLDER CLASS
    class MyViewHolder extends RecyclerView.ViewHolder{
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
            image.setOnClickListener(v->{
                int pos = getAdapterPosition();
                listener.onImageClick(itemView, pos);
            });
        }

        public void bind(Post post) {
            if (post != null) {
                nameTv.setText(post.getName());
                categoryTv.setText(post.getCategory());
                areaTv.setText(post.getAddress());

            /*
            if(Model.instance.getUid()!=member.getId()) {
                editBtn.setVisibility(View.GONE);
            }
             */

                image.setImageResource(R.drawable.avatarsmith);
                if (post.getImage() != null) {
                    Picasso.get()
                            .load(post.getImage())
                            .into(image);
                }
            }
        }
    }

    interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onImageClick(View v, int position);
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
            List<Post> postList = null;
            if(categoryName.equals("") && userId.equals("")) {
                Post post = viewModel.getData().getValue().get(position);
                holder.bind(post);
                return;
            } else  if (userId.equals("")){
                postList = viewModel.getData().getValue();
                List<Post> tempList= new ArrayList<Post>();
                for(Post p: postList) {
                    if (p.getCategory().equals(categoryName))
                        tempList.add(p);
                    }
                postList.removeAll(postList);
                postList.addAll(tempList);
                } else {
                    postList = viewModel.getData().getValue();
                    List<Post> tempList= new ArrayList<Post>();
                    for (Post p : postList) {
                        if (p.getUserId().equals(userId))
                            tempList.add(p);
                    }
                postList.removeAll(postList);
                postList.addAll(tempList);
                }
                Post post = postList.get(position);
                holder.bind(post);
            }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null) {
                return 0;
            }
            if (categoryName.equals("") && userId.equals("")) {
                return viewModel.getData().getValue().size();
            } else if (userId.equals("")){
                int count = 0;
                List<Post> postList = viewModel.getData().getValue();
                for(Post p: postList) {
                    if(p.getCategory().equals(categoryName))
                        count++;
                }
                return count;
            } else {
                int count = 0;
                List<Post> postList = viewModel.getData().getValue();
                for(Post p: postList) {
                    if(p.getUserId().equals(userId))
                        count++;
                }
                return count;
            }

        }
    }
}
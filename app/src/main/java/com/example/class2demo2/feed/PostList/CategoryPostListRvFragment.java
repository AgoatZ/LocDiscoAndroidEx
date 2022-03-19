package com.example.class2demo2.feed.PostList;

import android.content.Context;
import android.os.Bundle;
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
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;
import com.squareup.picasso.Picasso;


public class CategoryPostListRvFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME";

    private String categoryName;


    public CategoryPostListRvFragment() {
        // Required empty public constructor
    }

    public static CategoryPostListRvFragment newInstance(String categoryName) {
        CategoryPostListRvFragment fragment = new CategoryPostListRvFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
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
        View view = inflater.inflate(R.layout.fragment_category_post_list_rv, container, false);
        categoryName = CategoryPostListRvFragmentArgs.fromBundle(getArguments()).getCategoryName();

        if (!categoryName.equals("")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(categoryName);
        }
        //setting the recycler view
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.category_postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> {
            Model.instance.refreshPostsList();
            Model.instance.refreshPostsByCategory(categoryName);
        });

        listRv = view.findViewById(R.id.category_postlist_rv);
        listRv.setHasFixedSize(true);
        listRv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        listRv.setAdapter(adapter);

        //setting the adapter listeners
        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getDataByCategory(categoryName).getValue().get(position).getId();
            String postUId = viewModel.getDataByCategory(categoryName).getValue().get(position).getUserId();
            Navigation.findNavController(v).navigate(CategoryPostListRvFragmentDirections.actionGlobalPostFragment(postId, postUId));
        });

        viewModel.getDataByCategory(categoryName).observe(getViewLifecycleOwner(), list -> refresh());

        swipeRefresh.setRefreshing(Model.instance.getPostsListLoadingState().getValue() == Model.PostsListLoadingState.loading);
        Model.instance.getPostsListLoadingState().observe(getViewLifecycleOwner(), postsListLoadingState -> {
            swipeRefresh.setRefreshing(Model.instance.getPostsListLoadingState().getValue() == Model.PostsListLoadingState.loading);
        });
        Model.instance.refreshPostsList();
        Model.instance.refreshPostsByCategory(categoryName);
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }


    //HOLDER CLASS
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView categoryTv;
        TextView areaTv;
        ImageView image;

        MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
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
            Post post = viewModel.getDataByCategory(categoryName).getValue().get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getDataByCategory(categoryName).getValue() == null) {
                return 0;
            }
            return viewModel.getDataByCategory(categoryName).getValue().size();
        }
    }
}
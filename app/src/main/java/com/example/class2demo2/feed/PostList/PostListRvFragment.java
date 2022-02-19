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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.class2demo2.R;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;
import com.example.class2demo2.model.Student;
import com.squareup.picasso.Picasso;


public class PostListRvFragment extends Fragment {

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
                String stId = viewModel.getData().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionGlobalPostFragment(stId));
            }

            @Override
            public void onImageClick(View v, int position) {
                String stId = viewModel.getData().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionGlobalPostFragment(stId));
            }
        });
        viewModel.getData().observe(getViewLifecycleOwner(), list -> refresh());

        swipeRefresh.setRefreshing(Model.instance.getPostsListLoadingState().getValue() == Model.PostsListLoadingState.loading);
        Model.instance.getPostsListLoadingState().observe(getViewLifecycleOwner(), postsListLoadingState -> {
            swipeRefresh.setRefreshing(Model.instance.getPostsListLoadingState().getValue() == Model.PostsListLoadingState.loading);
        });
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

        //TODO: change student to post
        public void bind(Post post) {
            nameTv.setText(post.getName());
            categoryTv.setText(post.getCategory());
            areaTv.setText(post.getAddress());

            /*
            if(Model.instance.getUid()!=student.getId()) {
                editBtn.setVisibility(View.GONE);
            }
             */

            image.setImageResource(R.drawable.avatarsmith);
            if(post.getImage()!=null) {
                Picasso.get()
                        .load(post.getImage())
                        .into(image);
            }
        }
    }

    interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onImageClick(View v, int position);
    }

    //ADAPTER CLASS
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row,parent,false);
            MyViewHolder holder = new MyViewHolder(view, listener);

            return holder;
        }

        //TODO: change student to post
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = viewModel.getData().getValue().get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData().getValue() == null){
                return 0;
            }
            return viewModel.getData().getValue().size(); }

    }
}
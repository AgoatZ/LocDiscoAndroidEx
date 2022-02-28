package com.example.class2demo2.feed.CategoryList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.class2demo2.R;
import com.example.class2demo2.model.Category;
import com.example.class2demo2.model.Model;


public class CategoryListRvFragment extends Fragment {

    //MEMBERS
    CategoryListViewModel viewModel;
    MyAdapter adapter;
    RecyclerView listRv;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(CategoryListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list_rv, container, false);

        //SETTING THE RECYCLER VIEW
        swipeRefresh = view.findViewById(R.id.categorylist_swiperefresh);
        swipeRefresh.setOnRefreshListener(()->{
            Model.instance.refreshCategoriesList();
        });

        listRv = view.findViewById(R.id.categorylist_rv);
        listRv.setHasFixedSize(true);
        listRv.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        adapter = new MyAdapter();
        listRv.setAdapter(adapter);

        adapter.SetOnItemClickListener((v, position) ->{
            String categoryName = viewModel.getData().getValue().get(position).getName();
            Log.d("CATNAME: ", categoryName);
            Navigation.findNavController(v).navigate(CategoryListRvFragmentDirections.actionGlobalPostListRvFragment(categoryName));
        });

        viewModel.getData().observe(getViewLifecycleOwner(), list -> adapter.notifyDataSetChanged());

        swipeRefresh.setRefreshing(Model.instance.getCategoriesListLoadingState().getValue() == Model.CategoriesListLoadingState.loading);
        Model.instance.getCategoriesListLoadingState().observe(getViewLifecycleOwner(), categoriesListLoadingState -> {
            swipeRefresh.setRefreshing(Model.instance.getCategoriesListLoadingState().getValue() == Model.CategoriesListLoadingState.loading);
        });

        return view;
    }

    //HOLDER CLASS
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.categorylist_recyclerview_item_name_tv);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(itemView, pos);
            });
        }

        public void bind(Category category) {
            nameTv.setText(category.getName());
        }
    }

    interface OnItemClickListener{
        public void onItemClick(View v, int position);
    }

    //ADAPTER CLASS
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;

        public void SetOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.categorylist_recyclerview_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Category category = viewModel.getData().getValue().get(position);
            holder.bind(category);
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData().getValue() == null){
                return 0;
            }
            return viewModel.getData().getValue().size(); }
        }
    }
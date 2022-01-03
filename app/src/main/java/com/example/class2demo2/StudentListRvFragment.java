package com.example.class2demo2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

import java.util.List;


public class StudentListRvFragment extends Fragment {

    //MEMBERS
    StudentListRvViewModel viewModel;
    RecyclerView listRv;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(StudentListRvViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list,container,false);

        swipeRefresh = view.findViewById(R.id.studentlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() ->{
            refresh();
        });
        listRv = view.findViewById(R.id.studentlist_rv);
        listRv.setHasFixedSize(true);
        listRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        listRv.setAdapter(adapter);

        ImageButton add = view.findViewById(R.id.listfrag_plus_imgbtn);
        add.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_global_addFragment));

        adapter.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(View v, int position) {
                String stId = viewModel.getData().get(position).getId();
                Navigation.findNavController(v).navigate(StudentListRvFragmentDirections.actionStudentListRvFragmentToStudentDetailsFragment(stId));
            }


            @Override public void onCheckboxClick(int position, boolean isChecked){
                viewModel.getData().get(position).setFlag(isChecked);
            }
        });
        refresh();
        return view;
    }

    private void refresh() {
        swipeRefresh.setRefreshing(true);
        Model.instance.getAllStudents(list -> {
            viewModel.setData(list);
            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        });
    }


    //HOLDER CLASS
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv;
        TextView idTv;
        CheckBox cb;
        ImageView avatar;
        Button addBtn;
        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.listrow_name_tv);
            idTv = itemView.findViewById(R.id.listrow_id_tv);
            cb = itemView.findViewById(R.id.listrow_cb);
            avatar = itemView.findViewById(R.id.listrow_avatar_imv);
            addBtn = listRv.findViewById(R.id.listfrag_plus_imgbtn);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(itemView, pos);
            });
            cb.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onCheckboxClick(pos, cb.isChecked());
            });
        }
    }

    interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onCheckboxClick(int position, boolean isChecked);
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
            View view = getLayoutInflater().inflate(R.layout.student_list_row,parent,false);
            MyViewHolder holder = new MyViewHolder(view, listener);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            Student student = viewModel.getData().get(position);
            holder.nameTv.setText(student.getName());
            holder.idTv.setText(student.getId());
            holder.cb.setChecked(student.isFlag());
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData()==null){
                return 0;
            }
            return viewModel.getData().size(); }

    }
}
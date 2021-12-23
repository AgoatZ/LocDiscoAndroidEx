package com.example.class2demo2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

import java.util.List;

public class StudentListRvFragment extends Fragment {

    //MEMBERS
    List<Student> data;
    RecyclerView listRv;
    MyAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list,container,false);
        data= Model.instance.getAllStudents();

        listRv = view.findViewById(R.id.studentlist_rv);
        listRv.setHasFixedSize(true);
        listRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        listRv.setAdapter(adapter);

        ImageButton add = view.findViewById(R.id.listfrag_plus_imgbtn);
        add.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_studentListRvFragment_to_addFragment));

        adapter.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(View v, int position) {
                String stId = data.get(position).getId();
                Navigation.findNavController(v).navigate(StudentListRvFragmentDirections.actionStudentListRvFragmentToStudentDetailsFragment(stId));
            }


            @Override public void onCheckboxClick(int position, boolean isChecked){
                data.get(position).setFlag(isChecked);
            }
/*
            @Override
            public void onAddBtnClick(View v) {
                //Navigation.createNavigateOnClickListener(R.id.action_studentListRvFragment_to_studentDetailsFragment);
                Navigation.findNavController(view).navigate(StudentListRvFragmentDirections.actionStudentListRvFragmentToAddFragment());
                //Navigation.createNavigateOnClickListener(StudentListRvFragmentDirections.actionStudentListRvFragmentToAddFragment());
            }

 */
        });
        //ImageButton add = view.findViewById(R.id.listfrag_plus_imgbtn);
        //add.setOnClickListener(v->{
          //  Navigation.findNavController(v).navigate(R.id.action_studentListRvFragment_to_studentDetailsFragment);
        //});
        //add.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_studentListRvFragment_to_studentDetailsFragment));
        //setHasOptionsMenu(true);
        return view;
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
            /*
            if(addBtn!=null) {
                addBtn.setOnClickListener(v -> {
                    listener.onAddBtnClick(v);
                });
            }

             */


        }
    }

    interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onCheckboxClick(int position, boolean isChecked);
        //void onAddBtnClick(View v);
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

            Student student = data.get(position);
            holder.nameTv.setText(student.getName());
            holder.idTv.setText(student.getId());
            holder.cb.setChecked(student.isFlag());
        }

        @Override
        public int getItemCount() {
            if(data==null){
                return 0;
            }
            return data.size(); }

    }

    /*
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.student_list_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            Log.d("TAG","BLAR");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

     */
}
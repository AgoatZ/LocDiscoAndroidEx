package com.example.class2demo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

import java.util.List;

public class StudentListRvActivity extends AppCompatActivity {

    List<Student> data;
    RecyclerView listRv;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_rv);

        data= Model.instance.getAllStudents();

        listRv = findViewById(R.id.studentlist_rv);
        listRv.setHasFixedSize(true);
        listRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        listRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener()
         {
            @Override public void onItemClick(int position){

            }
            @Override public void onCheckboxClick(int position, boolean isChecked){
                data.get(position).setFlag(isChecked);
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv;
        TextView idTv;
        CheckBox cb;
        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
             nameTv = itemView.findViewById(R.id.listrow_name_tv);
             idTv = itemView.findViewById(R.id.listrow_id_tv);
             cb = itemView.findViewById(R.id.listrow_cb);
             itemView.setOnClickListener(v -> {
                 int pos = getAdapterPosition();
                 listener.onItemClick(pos);
             });
             cb.setOnClickListener(v -> {
                     int pos = getAdapterPosition();
                     listener.onCheckboxClick(pos, cb.isChecked());
             });
        }
    }

    interface OnItemClickListener{
        void onItemClick(int position);
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

            Student student = data.get(position);
            holder.nameTv.setText(student.getName());
            holder.idTv.setText(student.getId());
            holder.cb.setChecked(student.isFlag());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
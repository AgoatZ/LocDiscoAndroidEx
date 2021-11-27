package com.example.class2demo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

import java.util.List;

public class StudentListRvActivity extends AppCompatActivity {

    List<Student> data;
    RecyclerView listRv;
    MyAdapter adapter;
    Intent detailsIntent;
    Intent addIntent;
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
                detailsIntent = new
                        Intent(getApplicationContext(),
                        StudentDetailsActivity.class);
                detailsIntent.putExtra("nameTv",data.get(position).getName());
                detailsIntent.putExtra("idTv",data.get(position).getId());
                detailsIntent.putExtra("phoneTv",data.get(position).getPhone());
                detailsIntent.putExtra("addressTv",data.get(position).getAddress());
                detailsIntent.putExtra("cb",data.get(position).isFlag());
                detailsIntent.putExtra("avatarIv",data.get(position).getAvatar());
                startActivity(detailsIntent);
            }
            @Override public void onCheckboxClick(int position, boolean isChecked){
                data.get(position).setFlag(isChecked);
            }

             @Override
             public void onAddBtnClick(int position) {
                 addIntent = new
                         Intent(getApplicationContext(),
                         AddStudentActivity.class);
                 startActivity(addIntent);
             }
         });

    }


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
             itemView.setOnClickListener(v -> {
                 int pos = getAdapterPosition();
                 listener.onItemClick(pos);
             });
             cb.setOnClickListener(v -> {
                     int pos = getAdapterPosition();
                     listener.onCheckboxClick(pos, cb.isChecked());
             });
             addBtn.setOnClickListener(v->{
                 int pos = getAdapterPosition();
                 listener.onAddBtnClick(pos);
             });
        }
    }

    interface OnItemClickListener{
        void onItemClick(int position);
        void onCheckboxClick(int position, boolean isChecked);
        void onAddBtnClick(int position);
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
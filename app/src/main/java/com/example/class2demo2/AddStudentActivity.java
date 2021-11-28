package com.example.class2demo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

public class AddStudentActivity extends AppCompatActivity {
    Student student;
    EditText nameTv;
    EditText idTv;
    EditText phoneTv;
    EditText addressTv;
    Button cancelBtn;
    Button saveBtn;
    CheckBox cb;
    ImageView avatar;
    Intent detailsIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        detailsIntent = new Intent(this,
                StudentDetailsActivity.class);
         nameTv = findViewById(R.id.add_name_txt);
         idTv =findViewById(R.id.add_id_txt);
         phoneTv =findViewById(R.id.add_phone_txt);
         addressTv =findViewById(R.id.add_address_txt);
         cb =findViewById(R.id.add_checked_chk);
         avatar =findViewById(R.id.add_student_imgv);
         cancelBtn = findViewById(R.id.add_cancel_btn);
         saveBtn = findViewById(R.id.add_save_btn);
         cancelBtn.setOnClickListener(v -> {
             startActivity(detailsIntent);
         });
         saveBtn.setOnClickListener(v -> {
             student = new Student(nameTv.getText().toString(),idTv.getText().toString(),phoneTv.getText().toString(),addressTv.getText().toString(),cb.isChecked(),avatar.getId());
             Model.instance.addStudent(student);
             detailsIntent.putExtra("pos",Model.instance.getAllStudents().indexOf(student));
             startActivity(detailsIntent);
         });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
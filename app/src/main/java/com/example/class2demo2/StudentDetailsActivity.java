package com.example.class2demo2;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

public class StudentDetailsActivity extends AppCompatActivity {
    Student student;
    TextView nameTv;
    TextView idTv;
    TextView phoneTv;
    TextView addressTv;
    CheckBox cb;
    Button editBtn;
    ImageView avatar;
    Intent EditIntent;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        EditIntent = new Intent(this,
                EditStudentActivity.class);
        nameTv = findViewById(R.id.details_name_txt);
        idTv = findViewById(R.id.details_id_txt);
        phoneTv = findViewById(R.id.details_phone_txt);
        addressTv = findViewById(R.id.details_address_txt);
        cb = findViewById(R.id.details_checked_chk);
        avatar = findViewById(R.id.details_student_imgv);
        editBtn=findViewById(R.id.details_to_edit_btn);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pos = extras.getInt("pos");
            student = Model.instance.getAllStudents().get(pos);
            nameTv.setText(student.getName());
            idTv.setText(student.getId());
            addressTv.setText(student.getAddress());
            phoneTv.setText(student.getPhone());
            cb.setChecked(student.isFlag());
            avatar.setImageResource(student.getAvatar());
        }
        editBtn.setOnClickListener(v -> {
            EditIntent.putExtra("pos",pos);
            startActivity(EditIntent);
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
package com.example.class2demo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

public class EditStudentActivity extends AppCompatActivity {
    Student student;
    EditText name;
    EditText id;
    EditText phone;
    EditText address;
    CheckBox checked;
    ImageView avatar;
    Button cancelBtn;
    Button saveBtn;
    Button deleteBtn;
    Intent saveIntent;
    Intent cancelIntent;
    Intent deleteIntent;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        name = findViewById(R.id.edit_name_txt);
        id = findViewById(R.id.edit_id_txt);
        phone = findViewById(R.id.edit_phone_txt);
        address = findViewById(R.id.edit_address_txt);
        checked = findViewById(R.id.edit_checked_chk);
        avatar = findViewById(R.id.details_student_imgv);
        saveBtn=findViewById(R.id.edit_save_btn);
        cancelBtn=findViewById(R.id.edit_cancel_btn);
        deleteBtn=findViewById(R.id.edit_delete_btn);
        cancelIntent = new Intent(this,
                StudentListRvActivity.class);
        deleteIntent = new Intent(this,
                StudentListRvActivity.class);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pos = extras.getInt("pos");
            student = Model.instance.getAllStudents().get(pos);
            name.setText(student.getName());
            id.setText(student.getId());
            address.setText(student.getAddress());
            phone.setText(student.getPhone());
            checked.setChecked(student.isFlag());
            avatar.setImageResource(student.getAvatar());
        }
        saveBtn.setOnClickListener(v -> {


            student = Model.instance.getAllStudents().get(pos);
            student.setAddress(address.getText().toString());
            student.setId(id.getText().toString());
            student.setAvatar(avatar.getId());
            student.setFlag(checked.isChecked());
            student.setName(name.getText().toString());
            student.setPhone(phone.getText().toString());
            saveIntent = new Intent(v.getContext(),
                    StudentDetailsActivity.class);
            saveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            saveIntent.putExtra("pos",pos);
            startActivity(saveIntent);
        });
        cancelBtn.setOnClickListener(v -> {

            startActivity(cancelIntent);
        });
        deleteBtn.setOnClickListener(v -> {
            Model.instance.getAllStudents().remove(pos);
            startActivity(deleteIntent);
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
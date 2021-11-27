package com.example.class2demo2;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentDetailsActivity extends AppCompatActivity {
    TextView nameTv;
    TextView idTv;
    TextView phoneTv;
    TextView addressTv;
    CheckBox cb;
    ImageView avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        nameTv = findViewById(R.id.details_name_txt);
        idTv = findViewById(R.id.details_id_txt);
        phoneTv = findViewById(R.id.details_phone_txt);
        addressTv = findViewById(R.id.details_address_txt);
        cb = findViewById(R.id.details_checked_chk);
        avatar = findViewById(R.id.listrow_avatar_imv);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameTv.setText(extras.getString("nameTv"));
            idTv.setText(extras.getString("idTv"));
            addressTv.setText(extras.getString("addressTv"));
            phoneTv.setText(extras.getString("phoneTv"));
            cb.setChecked(extras.getBoolean("cb"));
            ///////////7avatar.setImageResource( (extras.getInt("avatarIv")));
        }
    }
}
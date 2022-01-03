package com.example.class2demo2;

import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Student;

import java.util.List;

public class StudentListRvViewModel extends ViewModel {
    List<Student> data;

    public List<Student> getData() {
        return data;
    }

    public void setData(List<Student> data) {
        this.data = data;
    }


}

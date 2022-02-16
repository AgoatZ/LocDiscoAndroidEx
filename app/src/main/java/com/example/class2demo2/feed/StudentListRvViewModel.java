package com.example.class2demo2.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

import java.util.List;

public class StudentListRvViewModel extends ViewModel {
    LiveData<List<Student>> data;

    public StudentListRvViewModel(){
        data = Model.instance.getAllStudents();
    }

    public LiveData<List<Student>> getData() {
        return data;
    }
}

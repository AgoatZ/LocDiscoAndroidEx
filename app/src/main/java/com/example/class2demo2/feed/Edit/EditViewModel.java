package com.example.class2demo2.feed.Edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

import java.util.List;

public class EditViewModel extends ViewModel {
    LiveData<Student> data;

    public EditViewModel() {}

    public LiveData<Student> getData(String id) {
        data = Model.instance.getStudentById(id);
        return data;
    }}

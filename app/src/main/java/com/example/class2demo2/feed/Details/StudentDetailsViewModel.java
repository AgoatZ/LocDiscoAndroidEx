package com.example.class2demo2.feed.Details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

public class StudentDetailsViewModel extends ViewModel {
    LiveData<Student> data;

    public StudentDetailsViewModel(){data = new MutableLiveData<>();}

    public LiveData<Student> getData(String id) {
        data = Model.instance.getStudentById(id);
        return data;
    }
}

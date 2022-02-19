package com.example.class2demo2.feed.Post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Student;

public class PostViewModel extends ViewModel {
    LiveData<Student> data;

    public PostViewModel() {data = new MutableLiveData<>();}

    public LiveData<Student> getData(String id) {
        data = Model.instance.getStudentById(id);
        return data;
    }
}

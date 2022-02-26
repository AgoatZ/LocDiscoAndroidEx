package com.example.class2demo2.feed.AddPost;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Category;
import com.example.class2demo2.model.Model;

import java.util.List;

public class AddPostViewModel extends ViewModel {
    LiveData<List<Category>> data;

    public AddPostViewModel() {
        data = Model.instance.getAllCategories();
    }

    public LiveData<List<Category>> getData() {
        return data;
    }

}

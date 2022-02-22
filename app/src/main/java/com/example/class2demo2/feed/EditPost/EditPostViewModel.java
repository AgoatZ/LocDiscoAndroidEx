package com.example.class2demo2.feed.EditPost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Member;
import com.example.class2demo2.model.Post;

import java.util.List;

public class EditPostViewModel extends ViewModel {
    LiveData<Post> data;

    public EditPostViewModel() {}

    public LiveData<Post> getData(String id) {
        data = Model.instance.getPostById(id);
        return data;
    }}

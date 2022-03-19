package com.example.class2demo2.feed.Post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;

public class PostViewModel extends ViewModel {
    LiveData<Post> data;

    public PostViewModel() {
        data = new MutableLiveData<>();
    }

    public LiveData<Post> getData(String id) {
        data = Model.instance.getPostById(id);
        return data;
    }
}

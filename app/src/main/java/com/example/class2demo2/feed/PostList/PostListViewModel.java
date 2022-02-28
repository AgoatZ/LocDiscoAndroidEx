package com.example.class2demo2.feed.PostList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;
import com.example.class2demo2.model.Member;

import java.util.List;

public class PostListViewModel extends ViewModel {
    LiveData<List<Post>> data;

    public PostListViewModel(){
        data = Model.instance.getAllPosts();
    }

    public LiveData<List<Post>> getData() { return data; }
}

package com.example.class2demo2.feed.PostList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;
import com.example.class2demo2.model.Member;

import java.util.ArrayList;
import java.util.List;

public class PostListViewModel extends ViewModel {
    LiveData<List<Post>> data;
    LiveData<List<Post>> dataByCategory;
    LiveData<List<Post>> dataByMember;

    public PostListViewModel(){
        data = Model.instance.getAllPosts();
        dataByCategory = Model.instance.getPostsByCategory("");
        dataByMember = Model.instance.getPostsByMember("");
    }

    public LiveData<List<Post>> getData() { return data; }

    public LiveData<List<Post>> getDataByCategory(String category) { return dataByCategory; }

    public LiveData<List<Post>> getDataByMember(String memberId) { return dataByMember; }
}

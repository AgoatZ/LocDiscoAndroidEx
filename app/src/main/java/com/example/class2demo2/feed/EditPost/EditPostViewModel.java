package com.example.class2demo2.feed.EditPost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Category;
import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Post;

import java.util.List;

public class EditPostViewModel extends ViewModel {
    LiveData<Post> data;
    LiveData<List<Category>> categories;

    public EditPostViewModel() {
        data = new MutableLiveData<Post>();
        categories = Model.instance.getAllCategories();
    }

    public LiveData<Post> getData(String id) {
        data = Model.instance.getPostById(id);
        return data;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }
}

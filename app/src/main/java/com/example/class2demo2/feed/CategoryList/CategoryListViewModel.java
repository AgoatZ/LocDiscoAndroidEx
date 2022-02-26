package com.example.class2demo2.feed.CategoryList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Category;
import com.example.class2demo2.model.Model;

import java.util.List;

public class CategoryListViewModel extends ViewModel {
    LiveData<List<Category>> data;

    public CategoryListViewModel() {
        data = Model.instance.getAllCategories();
    }

    public LiveData<List<Category>> getData() {
        return data;
    }
}

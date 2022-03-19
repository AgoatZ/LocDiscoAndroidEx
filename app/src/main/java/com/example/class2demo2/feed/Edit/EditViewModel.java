package com.example.class2demo2.feed.Edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Member;
import com.example.class2demo2.model.Model;

public class EditViewModel extends ViewModel {
    LiveData<Member> data;

    public EditViewModel() {
        data = new MutableLiveData<Member>();
    }

    public LiveData<Member> getData(String id) {
        data = Model.instance.getMemberById(id);
        return data;
    }
}

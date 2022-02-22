package com.example.class2demo2.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MemberViewModel extends ViewModel {
    LiveData<List<Member>> data;

    public MemberViewModel() {
        data = Model.instance.getAllMembers();
    }

    public LiveData<List<Member>> getData() {
        return data;
    }
}

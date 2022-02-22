package com.example.class2demo2.feed.MemberList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Member;

import java.util.List;

public class MemberListRvViewModel extends ViewModel {
    LiveData<List<Member>> data;

    public MemberListRvViewModel() {
        data = Model.instance.getAllMembers();
    }

    public LiveData<List<Member>> getData() {
        return data;
    }

}

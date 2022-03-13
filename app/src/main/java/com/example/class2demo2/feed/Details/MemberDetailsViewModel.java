package com.example.class2demo2.feed.Details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.class2demo2.model.Model;
import com.example.class2demo2.model.Member;

public class MemberDetailsViewModel extends ViewModel {
    LiveData<Member> data;

    public MemberDetailsViewModel(){data = new MutableLiveData<Member>();}

    public LiveData<Member> getData(String id) {
        data = Model.instance.getMemberById(id);
        return data;
    }
}

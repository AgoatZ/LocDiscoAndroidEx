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
    MutableLiveData<List<Post>> dataByCategory;
    MutableLiveData<List<Post>> dataByMember;

    public PostListViewModel(){
        data = Model.instance.getAllPosts();
        dataByCategory = new MutableLiveData<List<Post>>();
        dataByMember = new MutableLiveData<List<Post>>();
    }

    public LiveData<List<Post>> getData() { return data; }
    public LiveData<List<Post>> getDataByCategory(String category) {
        if (dataByCategory.getValue() == null || dataByCategory.getValue().size() == 0 || !dataByCategory.getValue().get(0).getCategory().equals(category)) {
            ArrayList<Post> filteredList = new ArrayList<Post>();
            if (data.getValue() != null) {
                for (Post post : data.getValue()) {
                    if (post.getCategory().equals(category)) {
                        filteredList.add(post);
                    }
                }
            }
            dataByCategory.postValue(filteredList);
        }
        return dataByCategory;
    }

    public LiveData<List<Post>> getDataByMember(String memberId) {
        if (dataByMember.getValue() == null || dataByMember.getValue().size() == 0 || !dataByMember.getValue().get(0).getUserId().equals(memberId)) {
            ArrayList<Post> filteredList = new ArrayList<Post>();
            if (data.getValue() != null) {
                for (Post post : data.getValue()) {
                    if (post.getUserId().equals(memberId)) {
                        filteredList.add(post);
                    }
                }
            }
            dataByMember.postValue(filteredList);
        }
        return dataByMember;
    }
}

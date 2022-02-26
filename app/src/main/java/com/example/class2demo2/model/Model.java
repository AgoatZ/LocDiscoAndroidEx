package com.example.class2demo2.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Database;

import com.example.class2demo2.MyApplication;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.util.Log;

public class Model {
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    ModelFirebase modelFirebase = new ModelFirebase();

    MutableLiveData<List<Member>> membersList = new MutableLiveData<List<Member>>();



    public enum MembersListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<MembersListLoadingState> membersListLoadingState = new MutableLiveData<>();

    private Model() {
        membersListLoadingState.setValue(MembersListLoadingState.loaded);
        postsListLoadingState.setValue(PostsListLoadingState.loaded);
    }

    public MutableLiveData<MembersListLoadingState> getMembersListLoadingState() {
        return membersListLoadingState;
    }

    public LiveData<List<Member>> getAllMembers() {
        if (membersList.getValue() == null) {
            refreshMembersList();
        }
        return membersList;
    }

    public void refreshMembersList() {
        membersListLoadingState.setValue(MembersListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("MemberLastUpdateDate", 0);

        executor.execute(() -> {
            List<Member> updatedList = AppLocalDb.db.memberDao().getAllMembers();
            membersList.postValue(updatedList);
        });
        // firebase get all updates since last local update date
        modelFirebase.getAllMembers(lastUpdateDate, list -> {

            // add all records to the local db
            executor.execute(() -> {
                Long localUpdateDate = new Long(0);
                Log.d("TAG", "firebase returned " + list.size());
                for (Member member : list) {
                    if (!member.isDeleted())
                        AppLocalDb.db.memberDao().insertAll(member);
                    else
                        AppLocalDb.db.memberDao().delete(member);
                    if (localUpdateDate < member.getUpdateDate()) {
                        localUpdateDate = member.getUpdateDate();
                    }
                }

                // update last local update date
                MyApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("MemberLastUpdateDate", localUpdateDate)
                        .commit();

                // return all data to caller
                List<Member> updatedList = AppLocalDb.db.memberDao().getAllMembers();
                membersList.postValue(updatedList);
                membersListLoadingState.postValue(MembersListLoadingState.loaded);
            });
        });
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }
/*
    public interface GetMemberByIdListener{
        void onComplete(Member member);
    }

 */

    //TODO: PUT retval INSIDE
    MutableLiveData<Member> retMember = new MutableLiveData<Member>();

    public void refreshMemberDetails(String id) {
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("MemberLastUpdateDate", 0);
        modelFirebase.getMemberById(id, lastUpdateDate, member -> {
            retMember.setValue(member);
            executor.execute(() -> {
                Long localUpdateDate = new Long(0);
                if (!member.isDeleted())
                    AppLocalDb.db.memberDao().insertAll(member);
                Member s = AppLocalDb.db.memberDao().getMemberById(id);
                retMember.postValue(s);
            });
        });
    }

    public LiveData<Member> getMemberById(String id) {
        if (membersList.getValue() == null) {
            refreshMembersList();
        }
        refreshMembersList();
        if(membersList.getValue() != null) {
            for (Member member : membersList.getValue()) {
                if (member.getId().equals(id)) {
                    retMember.setValue(member);
                }
            }
        }
        return retMember;
    }

    public interface AddMemberListener {
        void onComplete();
    }

    public void addMember(Member member, AddMemberListener listener) {
        modelFirebase.addMember(member, () -> {
            refreshMembersList();
            listener.onComplete();
        });
    }

    public interface DeleteListener {
        void onComplete();
    }

    public void delete(Member member, DeleteListener listener) {
        modelFirebase.delete(member, listener);
    }

    public interface LogicalDeleteListener {
        void onComplete();
    }

    public void logicalDelete(Member member, LogicalDeleteListener listener) {
        modelFirebase.logicalDelete(member, listener);
    }

    /***************POST MODEL*****************/
    public enum PostsListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<PostsListLoadingState> postsListLoadingState = new MutableLiveData<>();
    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();
    public interface AddPostListener{
        void onComplete();
    }
    public void addPost(Post post, AddPostListener listener) {
        modelFirebase.addPost(post, () -> {
            refreshPostsList();
        });
        listener.onComplete();
    }

    public MutableLiveData<PostsListLoadingState> getPostsListLoadingState() {
        return postsListLoadingState;
    }

    public void refreshPostsList() {
        postsListLoadingState.setValue(PostsListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PostLastUpdateDate", 0);

        executor.execute(() -> {
            List<Post> updatedList = AppLocalDb.db.postDao().getAllPosts();
            postsList.postValue(updatedList);
        });
        // firebase get all updates since last local update date
        modelFirebase.getAllPosts(lastUpdateDate, list -> {

            // add all records to the local db
            executor.execute(() -> {
                Long localUpdateDate = new Long(0);
                Log.d("TAG", "firebase returned " + list.size());
                for (Post post : list) {
                    if (!post.isDeleted())
                        AppLocalDb.db.postDao().insertAll(post);
                    else
                        AppLocalDb.db.postDao().delete(post);
                    if (localUpdateDate < post.getUpdateDate()) {
                        localUpdateDate = post.getUpdateDate();
                    }
                }

                // update last local update date
                MyApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("PostLastUpdateDate", localUpdateDate)
                        .commit();

                // return all data to caller
                List<Post> updatedList = AppLocalDb.db.postDao().getAllPosts();
                postsList.postValue(updatedList);
                postsListLoadingState.postValue(PostsListLoadingState.loaded);
            });
        });
    }

    public LiveData<List<Post>> getAllPosts(){
        if(postsList == null){
            refreshPostsList();
        }
        return postsList;
    }

    public LiveData<List<Post>> getPostsByCategory(String category){
        if(postsList == null){
            refreshPostsList();
        }
        MutableLiveData<List<Post>> categoryWithPostsList = new MutableLiveData<List<Post>>();
        categoryWithPostsList.postValue(AppLocalDb.db.postDao().getPostsByCategory(category));

        return categoryWithPostsList;
    }

    //TODO: PUT retval INSIDE
    MutableLiveData<Post> retPost = new MutableLiveData<Post>();

    public LiveData<Post> getPostById(String id) {
        if (postsList.getValue() == null) {
            refreshPostsList();
        }
        refreshPostsList();
        for (Post post : postsList.getValue()) {
            if (post.getId().equals(id)) {
                retPost.setValue(post);
            }
        }
        return retPost;
    }

    /***********Category************/
    public void refreshCategoriesList() {
        //postsListLoadingState.setValue(PostsListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("CategoryLastUpdateDate", 0);

        executor.execute(() -> {
            List<Category> updatedList = AppLocalDb.db.categoryDao().getAllCategories();
            categoriesList.postValue(updatedList);
        });
        // firebase get all updates since last local update date
        modelFirebase.getAllCategories(lastUpdateDate, list -> {

            // add all records to the local db
            executor.execute(() -> {
                Long localUpdateDate = new Long(0);
                Log.d("TAG", "firebase returned " + list.size());
                for (Category category : list) {
                    if (!category.isDeleted())
                        AppLocalDb.db.categoryDao().insertAll(category);
                    else
                        AppLocalDb.db.categoryDao().delete(category);
                    if (localUpdateDate < category.getUpdateDate()) {
                        localUpdateDate = category.getUpdateDate();
                    }
                }

                // update last local update date
                MyApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("CategoryLastUpdateDate", localUpdateDate)
                        .commit();

                // return all data to caller
                List<Category> updatedList = AppLocalDb.db.categoryDao().getAllCategories();
                categoriesList.postValue(updatedList);
                //postsListLoadingState.postValue(PostsListLoadingState.loaded);
            });
        });
    }

    MutableLiveData<List<Category>> categoriesList = new MutableLiveData<List<Category>>();


    public interface AddCategoryListener {
        void onComplete();
    }

    public void addCategory(Category category, AddCategoryListener listener) {
        modelFirebase.addCategory(category, () -> {
            refreshCategoriesList();
        });
        listener.onComplete();
    }

    public LiveData<List<Category>> getAllCategories(){
        refreshCategoriesList();
        return categoriesList;
    }

    /********Authentication********/
    public interface SignInListener {
        void onComplete(FirebaseUser user, Exception error);
    }

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    public String getUid() {
        return modelFirebase.getUId();
    }

    public void signIn(@NonNull String email, @NonNull String password, SignInListener listener) {
        modelFirebase.signIn(email, password, listener);
    }

    public void signOut(){
        executor.execute(()->modelFirebase.signOut());
    }

    public interface RegisterListener{
        void onComplete(FirebaseUser user, Exception error);
    }

    public void register(@NonNull String email,@NonNull String password, RegisterListener listener) {
        modelFirebase.register(email, password, listener);
    }

}
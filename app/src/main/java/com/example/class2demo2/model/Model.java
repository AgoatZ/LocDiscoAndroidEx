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

import java.util.ArrayList;
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
        refreshMembersList();
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

    MutableLiveData<Member> retMember = new MutableLiveData<Member>();

    public LiveData<Member> getMemberById(String id) {
        retMember.postValue(null);
        if (membersList.getValue() == null) {
            refreshMembersList();
        }
        refreshMembersList();
        if(membersList.getValue() != null) {
            for (Member member : membersList.getValue()) {
                if (member.getId().equals(id)) {
                    retMember.postValue(member);
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
        modelFirebase.logicalDelete(member, () -> {
            refreshMembersList();
            listener.onComplete();
        });
    }

    public boolean isMemberDeletedFromDb(Member member){
        modelFirebase.getMemberById(member.getId(), member.getUpdateDate(), member1 -> {
            member.setDeleted(member1.isDeleted());
        });
        return member.isDeleted();
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
    public interface PostDeleteListener {
        void onComplete();
    }

    public void postDelete(Post post, PostDeleteListener listener) {
        modelFirebase.postDelete(post, listener);
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
        postsListLoadingState.postValue(PostsListLoadingState.loading);

        MutableLiveData<List<Post>> retList = new MutableLiveData<List<Post>>();
        ArrayList<Post> arr = new ArrayList<Post>();

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
                    if (!post.isDeleted() && post.getCategory().equals(category))
                        arr.add(post);
                    if (localUpdateDate < post.getUpdateDate()) {
                        localUpdateDate = post.getUpdateDate();
                    }
                }

                // return all data to caller
                retList.postValue(arr);
                postsListLoadingState.postValue(PostsListLoadingState.loaded);
            });
        });

        return retList;
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
    public enum CategoriesListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<List<Category>> categoriesList = new MutableLiveData<List<Category>>();
    MutableLiveData<CategoriesListLoadingState> categoriesListLoadingState = new MutableLiveData<CategoriesListLoadingState>();

    public void refreshCategoriesList() {
        categoriesListLoadingState.setValue(CategoriesListLoadingState.loading);

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
                categoriesListLoadingState.postValue(CategoriesListLoadingState.loaded);
            });
        });
    }


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

    public interface DeleteCategoryListener{
        void onComplete(Exception error);
    }

    public void deleteCategory(Category category,DeleteCategoryListener listener) {
        modelFirebase.deleteCategory(category,listener);
    }

    public MutableLiveData<CategoriesListLoadingState> getCategoriesListLoadingState(){ return categoriesListLoadingState; }

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
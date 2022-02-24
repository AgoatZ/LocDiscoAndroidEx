package com.example.class2demo2.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }


    public interface GetAllMembersListener{
        void onComplete(List<Member> list);
    }
    public void getAllMembers(Long lastUpdateDate, GetAllMembersListener listener) {
        db.collection(Member.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task ->{
                    List<Member> list = new LinkedList<Member>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult())
                        {
                            Member member = Member.create(doc.getData());
                            if(member != null)
                            {
                                list.add(member);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addMember(Member member, Model.AddMemberListener listener) {
        Map<String, Object> json = member.toJson();

// Add a new document with a generated ID
        db.collection(Member.COLLECTION_NAME)
                .document(member.getId())
                .set(json)
                .addOnSuccessListener(unused -> {
                        listener.onComplete();
                })
                .addOnFailureListener(e -> {
                        listener.onComplete();
                });
    }

    public interface GetMemberByIdListener{
        void onComplete(Member member);
    }

    public void getMemberById(String id, Long lastUpdateDate, GetMemberByIdListener listener) {
        db.collection(Member.COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task ->{
                    Member member = null;
                    if(task.isSuccessful() && task.getResult() != null && (Long)task.getResult().getData().get("updateDate") >= lastUpdateDate){
                        member = Member.create(task.getResult().getData());
                        listener.onComplete(member);
                    }
                });
    }

    public void delete(Member member, Model.DeleteListener listener) {
        db.collection(Member.COLLECTION_NAME)
                .document(member.id)
                .delete()
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful() && task.getResult() != null){
                        listener.onComplete();
                    }
                });
    }

    public void logicalDelete(Member member, Model.LogicalDeleteListener listener){
        member.setDeleted(true);
        Map<String, Object> json = member.toJson();
        db.collection(Member.COLLECTION_NAME)
                .document(member.getId())
                .update(json)
                .addOnSuccessListener(unused -> {
                    listener.onComplete();
                })
                .addOnFailureListener(e -> {
                    listener.onComplete();
                });
    }

    /***************************POST MODEL*******************************/
    public interface GetAllPostsListener{
        void onComplete(List<Post> list);
    }
    public void getAllPosts(Long lastUpdateDate, GetAllPostsListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task ->{
                    List<Post> list = new LinkedList<Post>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult())
                        {
                            Post post = Post.create(doc.getData());
                            if(post != null)
                            {
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addPost(Post post, Model.AddPostListener listener) {
        Map<String, Object> json = post.toJson();

// Add a new document with a generated ID
        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnSuccessListener(unused -> {
                    listener.onComplete();
                })
                .addOnFailureListener(e -> {
                    listener.onComplete();
                });
    }

    /***************************CATEGORY MODEL*******************************/
    public interface GetAllCategoriesListener{
        void onComplete(List<Category> list);
    }
    public void getAllCategories(Long lastUpdateDate, GetAllCategoriesListener listener) {
        db.collection(Category.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task ->{
                    List<Category> list = new LinkedList<Category>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult())
                        {
                            Category category = Category.create(doc.getData());
                            if(category != null)
                            {
                                list.add(category);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addCategory(Category category, Model.AddCategoryListener listener) {
        Map<String, Object> json = category.toJson();

        // Add a new document with a generated ID
        db.collection(Post.COLLECTION_NAME)
                .document(category.getName())
                .set(json)
                .addOnSuccessListener(unused -> {
                    listener.onComplete();
                })
                .addOnFailureListener(e -> {
                    listener.onComplete();
                });
    }

    /******************************STORAGE*******************************/
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void saveImage(Bitmap imageBitmap, String imageName, Model.SaveImageListener listener) {
        StorageReference storageReference = storage.getReference();
        StorageReference imageReference = storageReference.child("/user_avatars/" + imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageReference.putBytes(data);
        uploadTask.addOnFailureListener(e -> listener.onComplete(null));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Uri downloadUrl = uri;
                listener.onComplete(downloadUrl.toString());
            });
        });
    }

    /*********************Authentication*********************************/
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public boolean isSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }

    public void signIn(@NonNull String email, @NonNull String password, Model.SignInListener listener){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            listener.onComplete(user, null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            listener.onComplete(null, task.getException());
                        }
                });
    }

    public void signOut(){
        mAuth.signOut();
    }

    public String getUId(){
        return mAuth.getUid();
    }

    public void register(String email, String password, Model.RegisterListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            listener.onComplete(user, null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            listener.onComplete(null, task.getException());
                        }
                });
    }
}

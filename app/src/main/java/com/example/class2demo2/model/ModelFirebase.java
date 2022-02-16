package com.example.class2demo2.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.class2demo2.login.LoginActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
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


    public interface GetAllStudentsListener{
        void onComplete(List<Student> list);
    }
    public void getAllStudents(Long lastUpdateDate, GetAllStudentsListener listener) {
        db.collection(Student.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task ->{
                    List<Student> list = new LinkedList<Student>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult())
                        {
                            Student student = Student.create(doc.getData());
                            if(student != null)
                            {
                                list.add(student);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addStudent(Student student, Model.AddStudentListener listener) {
        Map<String, Object> json = student.toJson();

// Add a new document with a generated ID
        db.collection(Student.COLLECTION_NAME)
                .document(student.getId())
                .set(json)
                .addOnSuccessListener(unused -> {
                        listener.onComplete();
                })
                .addOnFailureListener(e -> {
                        listener.onComplete();
                });
    }

    public interface GetStudentByIdListener{
        void onComplete(Student student);
    }

    public void getStudentById(String id, Long lastUpdateDate, GetStudentByIdListener listener) {
        db.collection(Student.COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task ->{
                    Student student = null;
                    if(task.isSuccessful() && task.getResult() != null && (Long)task.getResult().getData().get("updateDate") >= lastUpdateDate){
                        student = Student.create(task.getResult().getData());
                        listener.onComplete(student);
                    }
                });
    }

    public void delete(Student student, Model.DeleteListener listener) {
        db.collection(Student.COLLECTION_NAME)
                .document(student.id)
                .delete()
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful() && task.getResult() != null){
                        listener.onComplete();
                    }
                });
    }

    public void logicalDelete(Student student, Model.LogicalDeleteListener listener){
        student.setDeleted(true);
        Map<String, Object> json = student.toJson();
        db.collection(Student.COLLECTION_NAME)
                .document(student.getId())
                .update(json)
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
                            listener.onComplete(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            listener.onComplete(null);
                        }
                });
    }

    public void signOut(){
        mAuth.signOut();
    }

    public String getUId(){
        return mAuth.getUid();
    }
}

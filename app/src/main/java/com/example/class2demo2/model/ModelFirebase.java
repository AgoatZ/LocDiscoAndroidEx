package com.example.class2demo2.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getAllStudents(Model.GetAllStudentsListener listener) {
        db.collection(Student.COLLECTION_NAME)
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

    public void getStudentById(String id, Model.GetStudentByIdListener listener) {
        db.collection(Student.COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task ->{
                    Student student = null;
                    if(task.isSuccessful() && task.getResult() != null){
                        student = Student.create(task.getResult().getData());
                    }
                    listener.onComplete(student);
                });
    }

    public void delete(Student student, Model.DeleteListener listener) {
    }
}

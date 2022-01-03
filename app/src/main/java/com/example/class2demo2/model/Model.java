package com.example.class2demo2.model;

import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.class2demo2.R;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;

public class Model {
    public static final Model instance = new Model();

    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    ModelFirebase modelFirebase = new ModelFirebase();
    private Model(){

    }

    public interface GetAllStudentsListener{
        void onComplete(List<Student> list);
    }

    public void getAllStudents(GetAllStudentsListener listener) {
        modelFirebase.getAllStudents(listener);
    }

    public interface GetStudentByIdListener{
        void onComplete(Student student);
    }
    public void getStudentById(String id, GetStudentByIdListener listener) {
        modelFirebase.getStudentById(id,listener);
    }

    public interface  AddStudentListener{
        void onComplete();
    }

    public void addStudent(Student student, AddStudentListener listener){
        modelFirebase.addStudent(student, listener);
    }

    public interface DeleteListener{
        void onComplete();
    }

    public void delete(Student student, DeleteListener listener){
        modelFirebase.delete(student, listener);
    }
}


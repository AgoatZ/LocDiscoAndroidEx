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

    private Model(){

    }

    public interface GetAllStudentsListener{
        void onComplete(List<Student> list);
    }

    public void getAllStudents(GetAllStudentsListener listener) {
        executor.execute(() -> {
            List<Student> list = AppLocalDb.db.studentDao().getAllStudents();
            mainThread.post(() -> {
                listener.onComplete(list);
            });
        });
    }

    public interface GetStudentByIdListener{
        void onComplete(Student student);
    }
    public void getStudentById(String id, GetStudentByIdListener listener) {
        executor.execute(()->{
            Student student = AppLocalDb.db.studentDao().getStudentById(id);
            mainThread.post(()->{
                listener.onComplete(student);
            });
        });
    }

    public interface  AddStudentListener{
        void onComplete();
    }

    public void addStudent(Student student, AddStudentListener listener){
        executor.execute(()->{
            AppLocalDb.db.studentDao().insertAll(student);
            mainThread.post(()->{
                listener.onComplete();
            });
        });
    }

    public interface DeleteListener{
        void onComplete();
    }

    public void delete(Student student, DeleteListener listener){
        executor.execute(()->{
            AppLocalDb.db.studentDao().delete(student);
            mainThread.post(()->{
                listener.onComplete();
            });
        });
    }
}


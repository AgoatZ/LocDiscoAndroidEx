package com.example.class2demo2.model;

import android.content.Context;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.class2demo2.MyApplication;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.util.Log;

public class Model {
    public static final Model instance = new Model();

    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    ModelFirebase modelFirebase = new ModelFirebase();

    MutableLiveData<List<Student>> studentsList = new MutableLiveData<List<Student>>();

    public enum StudentsListLoadingState{
        loading,
        loaded
    }

    MutableLiveData<StudentsListLoadingState> studentsListLoadingState = new MutableLiveData<>();

    private Model(){
        studentsListLoadingState.setValue(StudentsListLoadingState.loaded);
    }

    public MutableLiveData<StudentsListLoadingState> getStudentsListLoadingState() {
        return studentsListLoadingState;
    }

    public LiveData<List<Student>> getAllStudents(){
        if(studentsList.getValue() == null){
            refreshStudentsList();
        }
        return studentsList;
    }

    public void refreshStudentsList(){
        studentsListLoadingState.setValue(StudentsListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("StudentLastUpdateDate",0);
        // firebase get all updates since last local update date
        modelFirebase.getAllStudents(lastUpdateDate, list -> {
            // add all records to the local db
            executor.execute(()->{
            Long localUpdateDate = new Long(0);
            Log.d("TAG", "firebase returned " + list.size());
            for(Student student: list) {
                AppLocalDb.db.studentDao().insertAll(student);
                if(localUpdateDate < student.getUpdateDate()) {
                    localUpdateDate = student.getUpdateDate();
                }
            }
                // update last local update date
                MyApplication.getContext()
                    .getSharedPreferences("TAG",Context.MODE_PRIVATE)
                    .edit()
                    .putLong("StudentLastUpdateDate", localUpdateDate)
                    .commit();
                // return all data to caller
            List<Student> updatedList = AppLocalDb.db.studentDao().getAllStudents();
            studentsList.postValue(updatedList);
            studentsListLoadingState.postValue(StudentsListLoadingState.loaded);
        });
    });

        modelFirebase.getAllStudents(lastUpdateDate, list -> {
            studentsList.setValue(list);
            studentsListLoadingState.setValue(StudentsListLoadingState.loaded);
        });
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


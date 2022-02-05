package com.example.class2demo2.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;

import com.example.class2demo2.MyApplication;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.util.Log;

public class Model {
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

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

        executor.execute(()->{
            List<Student> updatedList = AppLocalDb.db.studentDao().getAllStudents();
            studentsList.postValue(updatedList);
        });
        // firebase get all updates since last local update date
        modelFirebase.getAllStudents(lastUpdateDate, list -> {

            // add all records to the local db
            executor.execute(()->{
            Long localUpdateDate = new Long(0);
            Log.d("TAG", "firebase returned " + list.size());
            for(Student student: list) {
                if(!student.isDeleted())
                    AppLocalDb.db.studentDao().insertAll(student);
                else
                    AppLocalDb.db.studentDao().delete(student);
                if(localUpdateDate < student.getUpdateDate()) {
                    localUpdateDate = student.getUpdateDate();
                }
            }
                // update last local update date
                //AppLocalDb.db.studentDao().delete(new Student("", "1234", "", "", false, null));
                //AppLocalDb.db.studentDao().delete(new Student("", "101010", "", "", false, null));
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
    }

    public interface SaveImageListener{
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }
/*
    public interface GetStudentByIdListener{
        void onComplete(Student student);
    }

 */

    MutableLiveData<Student> retStudent = new MutableLiveData<Student>();
    public void refreshStudentDetails(String id){
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("StudentLastUpdateDate",0);
        modelFirebase.getStudentById(id, lastUpdateDate, student -> {
            retStudent.setValue(student);
            executor.execute(()->{
                Long localUpdateDate = new Long(0);
                if(!student.isDeleted())
                    AppLocalDb.db.studentDao().insertAll(student);
                Student s = AppLocalDb.db.studentDao().getStudentById(id);
                retStudent.postValue(s);
            });
        });
    }

    public LiveData<Student> getStudentById(String id) {
        if(studentsList.getValue() == null){
            refreshStudentsList();
        }
        for(Student student: studentsList.getValue()){
            if(student.getId().equals(id)){
                retStudent.setValue(student);
            }
        }
        return retStudent;
    }

    public interface AddStudentListener{
        void onComplete();
    }

    public void addStudent(Student student, AddStudentListener listener){
        modelFirebase.addStudent(student, ()->{
            refreshStudentsList();
            listener.onComplete();
        });
    }

    public interface DeleteListener{
        void onComplete();
    }

    public void delete(Student student, DeleteListener listener){
        modelFirebase.delete(student, listener);
    }

    public interface LogicalDeleteListener{
        void onComplete();
    }

    public void logicalDelete(Student student, LogicalDeleteListener listener){
        modelFirebase.logicalDelete(student, listener);
    }

    /********Authentication********/
    public boolean isSignedIn(){
        return modelFirebase.isSignedIn();
    }
}


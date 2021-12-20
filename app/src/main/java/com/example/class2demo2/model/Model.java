package com.example.class2demo2.model;

import com.example.class2demo2.R;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model(){


    }

    List<Student> data = new LinkedList<Student>();

    public List<Student> getAllStudents() {
        return AppLocalDb.db.studentDao().getAll();
    }
    public void addStudent(Student student){
        data.add(student);
    }
}


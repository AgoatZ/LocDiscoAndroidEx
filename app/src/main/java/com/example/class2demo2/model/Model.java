package com.example.class2demo2.model;

import com.example.class2demo2.R;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model(){
        for(int i=0;i<100;i++){
            Student s = new Student("name",""+i, "052222"+i,"herzel"+i, false, R.drawable.avatarsmith);
            data.add(s);
        }
    }

    List<Student> data = new LinkedList<Student>();

    public List<Student> getAllStudents() {
        return data;
    }
    public void addStudent(Student student){
        data.add(student);
    }
}


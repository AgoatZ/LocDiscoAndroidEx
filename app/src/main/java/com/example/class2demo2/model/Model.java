package com.example.class2demo2.model;

import com.example.class2demo2.R;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model(){
        for(int i=0;i<10;i++){
            Student s = new Student("Agent Smith clone no."+i,""+i, "052497558"+i,"Telephone Booth "+i, false, R.drawable.avatarsmith);
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


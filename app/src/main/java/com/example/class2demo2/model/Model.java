package com.example.class2demo2.model;

import com.example.class2demo2.R;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model(){
        for(int i=0;i<10;i++)
        {
            Student s = new Student("Agent Smith","30098744"+i,"054472928"+i,"CellBox #"+i,false,R.drawable.avatarsmith);
            data.add(s);
        }

    }

    List<Student> data = new LinkedList<Student>();

    public Student getStudentById(String id) {
        for(Student s:data)
        {
            if(s.getId().equals(id))
            {
                return s;
            }
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return data;
    }
    /*{
        return AppLocalDb.db.studentDao().getAll();
    }*/
    public void addStudent(Student student){
        data.add(student);
    }
}


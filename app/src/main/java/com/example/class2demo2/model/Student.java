package com.example.class2demo2.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Student {
    final public static String COLLECTION_NAME = "students";
    @PrimaryKey
    @NonNull
    String id;
    String name;
    String phone;
    String address;
    String avatar;
    boolean flag;
    Long updateDate = new Long(0);
    boolean isDeleted;

    public Student(){}

    public Student(Student s){
        this.name = s.name;
        this.id = s.id;
        this.address = s.address;
        this.phone = s.phone;
        this.flag = s.flag;
        this.avatar = s.avatar;
        this.isDeleted = s.isDeleted;
    }

    public Student(String name, String id,String phone,String address, boolean flag,String avatar) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.flag = flag;
        this.avatar = avatar;
        this.isDeleted = false;

    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {

        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public static Student create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        String phone = (String) json.get("phone");
        String address = (String) json.get("address");
        String avatar = null;
        if(json.get("avatar") != null) {
            avatar = json.get("avatar").toString();
        }
        boolean flag = (boolean) json.get("flag");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        boolean isDeleted = (boolean) json.get("isDeleted");

        Student student = new Student(name,id,phone,address,flag,avatar);
        student.setUpdateDate(updateDate);
        student.setDeleted(isDeleted);
        return student;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("phone",phone);
        json.put("address",address);
        json.put("avatar",avatar);
        json.put("flag",flag);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("isDeleted",isDeleted);
        return json;
    }
//TODO:...
    public Long getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

}

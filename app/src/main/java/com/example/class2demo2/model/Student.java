package com.example.class2demo2.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    int avatar;
    boolean flag;

    public Student(){}

    public Student(String name, String id,String phone,String address, boolean flag,int avatar) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.flag = flag;
        this.avatar = avatar;

    }

    public static Student create(Map<String, Object> json) {
         String id = (String) json.get("id");
         String name = (String) json.get("name");
         String phone = (String) json.get("phone");
         String address = (String) json.get("address");
         long avatar = (long) json.get("avatar")*1;
         boolean flag = (boolean) json.get("flag");
         Student student = new Student(name,id,phone,address,flag,(int)avatar);
         return student;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
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

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("phone",phone);
        json.put("address",address);
        json.put("avatar",avatar);
        json.put("flag",flag);
        return json;
    }
}

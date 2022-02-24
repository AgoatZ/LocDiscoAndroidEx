package com.example.class2demo2.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Category {
    public static final String COLLECTION_NAME = "categories";
    @PrimaryKey
    @NonNull
    String name;
    Boolean isDeleted;
    Long updateDate = new Long(0);

    public Category(){}

    public Category(Category category){
        this.name = category.name;
        this.isDeleted = category.isDeleted;
    }

    public Category(String name){
        this.name = name;
        this.isDeleted = false;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public static Category create(Map<String, Object> json) {
        String name = (String) json.get("name");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        boolean isDeleted = (boolean) json.get("isDeleted");

        Category category = new Category(name);
        category.setUpdateDate(updateDate);
        category.setDeleted(isDeleted);
        return category;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("name",name);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("isDeleted",isDeleted);
        return json;
    }
}

package com.example.class2demo2.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {
    final public static String COLLECTION_NAME = "posts";
    @PrimaryKey
    @NonNull
    String id;
    @ForeignKey(entity = Member.class, parentColumns = {"id"}, childColumns = {"userId"})
    @NonNull
    String userId;        
    String name;
    String area;
    String address;
    String category;
    String image;
    String description;
    Long updateDate = new Long(0);
    boolean isDeleted;

    public Post(){}

    public Post(Post p){
        this.userId = p.userId;
        this.name = p.name;
        this.id = p.id;
        this.address = p.address;
        this.area = p.area;
        this.image = p.image;
        this.category = p.category;
        this.isDeleted = p.isDeleted;
        this.description = p.description;
    }

    public Post(String name, String id,String category,String address, String image, String area, String userId, String description) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.category = category;
        this.image = image;
        this.area = area;
        this.userId = userId;
        this.description = description;
        this.isDeleted = false;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        String phone = (String) json.get("phone");
        String address = (String) json.get("address");
        String userId = (String) json.get("userId");
        String area = (String) json.get("area");
        String category = (String) json.get("category");
        String description = (String) json.get("description");
        String image = null;
        if(json.get("image") != null) {
            image = json.get("image").toString();
        }
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        boolean isDeleted = (boolean) json.get("isDeleted");

        Post post = new Post(name, id, category, address, image, area, userId, description);
        post.setUpdateDate(updateDate);
        post.setDeleted(isDeleted);
        return post;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("area",area);
        json.put("address",address);
        json.put("image",image);
        json.put("userId",userId);
        json.put("category",category);
        json.put("description",description);
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

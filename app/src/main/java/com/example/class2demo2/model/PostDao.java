package com.example.class2demo2.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post")
    List<Post> getAllPosts();

    @Query("select * from Post where userId=:userId")
    Post getAllUserPosts(String userId);

    @Query("select * from Post where id =:id")
    Post getPostById(String id);

    @Query("select * from Post where category=:category")
    List<Post> getPostsByCategory(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post post);

}

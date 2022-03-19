package com.example.class2demo2.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemberDao {

    @Query("select * from Member")
    List<Member> getAllMembers();

    @Query("select * from Member where id =:sid")
    Member getMemberById(String sid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Member... members);

    @Delete
    void delete(Member member);
}

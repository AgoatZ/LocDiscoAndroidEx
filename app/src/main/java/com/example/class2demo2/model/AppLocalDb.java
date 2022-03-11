package com.example.class2demo2.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.class2demo2.MyApplication;

@Database(entities = {Member.class, Post.class, Category.class}, version = 11)
abstract class AppLocalDbRepository extends RoomDatabase{
    public abstract MemberDao memberDao();
    public abstract PostDao postDao();
    public abstract CategoryDao categoryDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "MembersDb.db")
            .fallbackToDestructiveMigration()
            .build();
}

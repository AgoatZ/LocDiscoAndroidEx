package com.example.class2demo2.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.class2demo2.MyApplication;

@Database(entities = {Student.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase{
    public abstract StudentDao studentDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "StudentsDb.db")
            .fallbackToDestructiveMigration()
            .build();
}

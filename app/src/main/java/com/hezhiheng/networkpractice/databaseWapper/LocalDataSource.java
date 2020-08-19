package com.hezhiheng.networkpractice.databaseWapper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hezhiheng.networkpractice.dao.PersonDao;
import com.hezhiheng.networkpractice.entity.PersonEntity;

@Database(entities = {PersonEntity.class}, version = 1)
public abstract class LocalDataSource extends RoomDatabase {
    public abstract PersonDao getPersonDao();
}

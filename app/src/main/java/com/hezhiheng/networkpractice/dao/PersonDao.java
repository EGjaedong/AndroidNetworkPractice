package com.hezhiheng.networkpractice.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hezhiheng.networkpractice.entity.PersonEntity;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("select * from person")
    List<PersonEntity> getAll();

    @Insert
    void insertAll(PersonEntity... persons);
}

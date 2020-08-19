package com.hezhiheng.networkpractice.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class PersonEntity {
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "avatar")
    public String avatar;

    public PersonEntity() {
    }

    public PersonEntity(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }
}

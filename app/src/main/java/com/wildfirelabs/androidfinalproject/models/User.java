package com.wildfirelabs.androidfinalproject.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String username;
    private String password;
    private boolean manager;

    public User(String name, String username, String password, boolean manager)
    {
        this.name = name;
        this.username = username;
        this.password = password;
        this.manager = manager;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean isManager()
    {
        return manager;
    }
}

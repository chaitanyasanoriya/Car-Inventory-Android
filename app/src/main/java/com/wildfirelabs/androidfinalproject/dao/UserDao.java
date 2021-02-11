package com.wildfirelabs.androidfinalproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wildfirelabs.androidfinalproject.models.User;

@Dao
public interface UserDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Query("Select * from user_table where username = :username and password = :password LIMIT 1")
    User getUser(String username, String password);

    @Query("Select Count(*) from user_table where username = :username")
    int getUsernameNumber(String username);
}

package com.wildfirelabs.androidfinalproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wildfirelabs.androidfinalproject.models.Cars;

import java.util.List;

@Dao
public interface CarsDao
{
    @Insert
    long insert(Cars car);

    @Update
    int update(Cars car);

    @Delete
    int delete(Cars car);

    @Query("Select * from cars_table ORDER BY name")
    LiveData<List<Cars>> getAllCars();

    @Query("Select * from cars_table where name Like :str or model Like :str or color Like :str or vin Like :str ORDER BY name")
    List<Cars> getSearchResults(String str);
}

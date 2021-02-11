package com.wildfirelabs.androidfinalproject.databases;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wildfirelabs.androidfinalproject.dao.CarsDao;
import com.wildfirelabs.androidfinalproject.dao.UserDao;
import com.wildfirelabs.androidfinalproject.models.Cars;
import com.wildfirelabs.androidfinalproject.models.User;

@Database(entities = {User.class, Cars.class}, version = 3)
public abstract class CarInventoryDatabase extends RoomDatabase
{
    private static CarInventoryDatabase instance;
    public abstract UserDao userDao();
    public abstract CarsDao carsDao();

    public static synchronized CarInventoryDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CarInventoryDatabase.class, "car_inventory_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db)
        {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private UserDao userDao;

        private PopulateDBAsyncTask(CarInventoryDatabase db)
        {
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            userDao.insert(new User("Manager","manager","9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08",true));
            return null;
        }
    }
}

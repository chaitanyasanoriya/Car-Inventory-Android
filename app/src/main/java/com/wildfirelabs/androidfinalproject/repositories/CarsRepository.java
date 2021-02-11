package com.wildfirelabs.androidfinalproject.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.wildfirelabs.androidfinalproject.databases.CarInventoryDatabase;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.CarsCUDCallbacks;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.CarsSearchCallback;
import com.wildfirelabs.androidfinalproject.dao.CarsDao;
import com.wildfirelabs.androidfinalproject.models.Cars;

import java.util.List;

public class CarsRepository
{
    private CarsDao carsDao;
    private LiveData<List<Cars>> allCars;

    public CarsRepository(Application application)
    {
        CarInventoryDatabase db = CarInventoryDatabase.getInstance(application);
        carsDao = db.carsDao();
        allCars = carsDao.getAllCars();
    }

    public void insert(Cars cars, CarsCUDCallbacks carsCUDCallbacks)
    {
        new CarsRepository.InsertCarsAsyncTask(carsDao, carsCUDCallbacks).execute(cars);
    }

    public void update(Cars cars, CarsCUDCallbacks carsCUDCallbacks)
    {
        new CarsRepository.UpdateCarsAsyncTask(carsDao, carsCUDCallbacks).execute(cars);
    }

    public void delete(Cars cars, CarsCUDCallbacks carsCUDCallbacks)
    {
        new CarsRepository.DeleteCarsAsyncTask(carsDao, carsCUDCallbacks).execute(cars);
    }

    public void searchCar(String s, CarsSearchCallback carsSearchCallback)
    {
        new CarsRepository.SearchCarsAsyncTask(carsDao,carsSearchCallback).execute(s);
    }

    public LiveData<List<Cars>> getAllCars()
    {
        return allCars;
    }

    private static class InsertCarsAsyncTask extends AsyncTask<Cars, Void, Void>
    {
        private CarsDao carsDao;
        private CarsCUDCallbacks carsCUDCallbacks;
        private long nrows = 0;

        private InsertCarsAsyncTask(CarsDao carsDao, CarsCUDCallbacks carsCUDCallbacks)
        {
            this.carsDao = carsDao;
            this.carsCUDCallbacks = carsCUDCallbacks;
        }

        @Override
        protected Void doInBackground(Cars... cars)
        {
            try
            {
                nrows = carsDao.insert(cars[0]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if(carsCUDCallbacks != null)
            {
                carsCUDCallbacks.insertedResult(nrows);
            }
        }
    }

    private static class UpdateCarsAsyncTask extends AsyncTask<Cars, Void, Void>
    {
        private CarsDao carsDao;
        private CarsCUDCallbacks carsCUDCallbacks;
        private int nrows = 0;

        private UpdateCarsAsyncTask(CarsDao carsDao, CarsCUDCallbacks carsCUDCallbacks)
        {
            this.carsDao = carsDao;
            this.carsCUDCallbacks = carsCUDCallbacks;
        }

        @Override
        protected Void doInBackground(Cars... cars)
        {
            nrows = carsDao.update(cars[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if(carsCUDCallbacks != null)
            {
                carsCUDCallbacks.updatedResult(nrows);
            }
        }
    }

    private static class DeleteCarsAsyncTask extends AsyncTask<Cars, Void, Void>
    {
        private CarsDao carsDao;
        private CarsCUDCallbacks carsCUDCallbacks;
        private int nrows = 0;

        private DeleteCarsAsyncTask(CarsDao carsDao, CarsCUDCallbacks carsCUDCallbacks)
        {
            this.carsDao = carsDao;
            this.carsCUDCallbacks = carsCUDCallbacks;
        }

        @Override
        protected Void doInBackground(Cars... cars)
        {
            nrows = carsDao.delete(cars[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if(carsCUDCallbacks != null)
            {
                carsCUDCallbacks.deletedResult(nrows);
            }
        }
    }

    private static class SearchCarsAsyncTask extends AsyncTask<String, Void, Void>
    {
        private CarsDao carsDao;
        private CarsSearchCallback mCarsSearchCallback;
        private List<Cars> mCars;

        private SearchCarsAsyncTask(CarsDao carsDao, CarsSearchCallback carsSearchCallback)
        {
            this.carsDao = carsDao;
            this.mCarsSearchCallback = carsSearchCallback;
        }

        @Override
        protected Void doInBackground(String... strings)
        {
            mCars = carsDao.getSearchResults(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            mCarsSearchCallback.searchCarsResult(mCars);
        }
    }
}

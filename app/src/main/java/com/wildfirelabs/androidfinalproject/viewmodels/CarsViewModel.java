package com.wildfirelabs.androidfinalproject.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wildfirelabs.androidfinalproject.callbackinterfaces.CarsCUDCallbacks;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.CarsSearchCallback;
import com.wildfirelabs.androidfinalproject.models.Cars;
import com.wildfirelabs.androidfinalproject.repositories.CarsRepository;

import java.util.List;

public class CarsViewModel extends AndroidViewModel
{
    private CarsRepository repository;
    private LiveData<List<Cars>> allCars;

    public CarsViewModel(@NonNull Application application)
    {
        super(application);
        repository = new CarsRepository(application);
        allCars = repository.getAllCars();
    }

    public void insert(Cars cars, CarsCUDCallbacks carsCUDCallbacks)
    {
        repository.insert(cars, carsCUDCallbacks);
    }

    public void update(Cars cars, CarsCUDCallbacks carsCUDCallbacks)
    {
        repository.update(cars, carsCUDCallbacks);
    }

    public void delete(Cars cars, CarsCUDCallbacks carsCUDCallbacks)
    {
        repository.delete(cars, carsCUDCallbacks);
    }

    public void searchCar(String s, CarsSearchCallback carsSearchCallback)
    {
        repository.searchCar(s,carsSearchCallback);
    }

    public LiveData<List<Cars>> getAllCars()
    {
        return allCars;
    }
}

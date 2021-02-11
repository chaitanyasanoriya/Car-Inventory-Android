package com.wildfirelabs.androidfinalproject.callbackinterfaces;

import com.wildfirelabs.androidfinalproject.models.Cars;

import java.util.List;

@FunctionalInterface
public interface CarsSearchCallback
{
    public void searchCarsResult(List<Cars> cars);
}

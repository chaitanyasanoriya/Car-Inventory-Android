package com.wildfirelabs.androidfinalproject.callbackinterfaces;

public interface CarsCUDCallbacks
{
    void insertedResult(long nrows);
    void updatedResult(int nrows);
    void deletedResult(int nrows);
}

package com.wildfirelabs.androidfinalproject.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.wildfirelabs.androidfinalproject.callbackinterfaces.UserSignInCallbacks;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.UserSignUpCallbacks;
import com.wildfirelabs.androidfinalproject.models.User;
import com.wildfirelabs.androidfinalproject.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel
{
    private UserRepository repository;

    public UserViewModel(@NonNull Application application)
    {
        super(application);
        repository = new UserRepository(application);
    }

    public void insert(User user, UserSignUpCallbacks userSignUpCallbacks)
    {
        repository.insert(user,userSignUpCallbacks);
    }

    public void getUser(String username, String password, UserSignInCallbacks userSignInCallbacks)
    {
        repository.getUser(username,password,userSignInCallbacks);
    }

    public void getUsernameNumber(String username, UserSignUpCallbacks userSignUpCallbacks)
    {
        repository.getUsernameNumber(username,userSignUpCallbacks);
    }
}

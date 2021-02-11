package com.wildfirelabs.androidfinalproject.callbackinterfaces;

import com.wildfirelabs.androidfinalproject.models.User;

@FunctionalInterface
public interface UserSignInCallbacks
{
    void userReturned(User user);
}

package com.wildfirelabs.androidfinalproject.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.wildfirelabs.androidfinalproject.databases.CarInventoryDatabase;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.UserSignInCallbacks;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.UserSignUpCallbacks;
import com.wildfirelabs.androidfinalproject.dao.UserDao;
import com.wildfirelabs.androidfinalproject.models.User;

public class UserRepository
{
    private UserDao userDao;

    public UserRepository(Application application)
    {
        CarInventoryDatabase database = CarInventoryDatabase.getInstance(application);
        userDao = database.userDao();
    }

    public void insert(User user, UserSignUpCallbacks userSignUpCallbacks)
    {
        new InsertUserAsyncTask(userDao, userSignUpCallbacks).execute(user);
    }

    public void getUser(String username, String password, UserSignInCallbacks userSignInCallbacks)
    {
        new GetSpecificUserAsyncTask(userDao, userSignInCallbacks).execute(username, password);
    }

    public void getUsernameNumber(String username, UserSignUpCallbacks userSignUpCallbacks)
    {
        new GetNumberOfUsersAsyncTask(userDao, userSignUpCallbacks).execute(username);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void>
    {
        private UserDao userDao;
        private UserSignUpCallbacks mUserSignUpCallbacks;
        private long mNum = 0;

        private InsertUserAsyncTask(UserDao userDao, UserSignUpCallbacks userSignUpCallbacks)
        {
            this.userDao = userDao;
            this.mUserSignUpCallbacks = userSignUpCallbacks;
        }

        @Override
        protected Void doInBackground(User... users)
        {
            mNum = userDao.insert(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if (mUserSignUpCallbacks != null)
            {
                mUserSignUpCallbacks.userSaved(mNum);
            }
        }
    }

    private static class GetSpecificUserAsyncTask extends AsyncTask<String, Void, Void>
    {
        private UserDao userDao;
        private UserSignInCallbacks mUserSignInCallbacks;
        private User mUser;

        private GetSpecificUserAsyncTask(UserDao userDao, UserSignInCallbacks userSignInCallbacks)
        {
            this.userDao = userDao;
            this.mUserSignInCallbacks = userSignInCallbacks;
        }

        @Override
        protected Void doInBackground(String... strings)
        {
            mUser = userDao.getUser(strings[0], strings[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            mUserSignInCallbacks.userReturned(mUser);
        }
    }

    private static class GetNumberOfUsersAsyncTask extends AsyncTask<String, Void, Void>
    {
        private UserDao userDao;
        private UserSignUpCallbacks mUserSignUpCallbacks;
        private int mNum = 0;

        private GetNumberOfUsersAsyncTask(UserDao userDao, UserSignUpCallbacks userSignUpCallbacks)
        {
            this.userDao = userDao;
            this.mUserSignUpCallbacks = userSignUpCallbacks;
        }

        @Override
        protected Void doInBackground(String... strings)
        {
            mNum = userDao.getUsernameNumber(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            mUserSignUpCallbacks.numberOfUsers(mNum);
        }
    }

}

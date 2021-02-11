package com.wildfirelabs.androidfinalproject.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.wildfirelabs.androidfinalproject.R;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.UserSignInCallbacks;
import com.wildfirelabs.androidfinalproject.viewmodels.UserViewModel;
import com.wildfirelabs.androidfinalproject.Utilities;
import com.wildfirelabs.androidfinalproject.models.User;

public class SignInActivity extends AppCompatActivity implements UserSignInCallbacks
{
    private EditText mUsernameEditText, mPasswordEditText;
    private Animation mShakeAnimation;
    private Vibrator mVibrator;
    private final int VIBRATION_PERIOD = 500;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mUsernameEditText = (EditText) findViewById(R.id.username_edittext);
        mPasswordEditText = (EditText) findViewById(R.id.password_edittext);
        mShakeAnimation = AnimationUtils.loadAnimation(SignInActivity.this, R.anim.shake);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        createManager();
    }

    private void createManager()
    {
        User user = new User("Manager", "manager", Utilities.digest("test"), true);
        mUserViewModel.insert(user, null);
    }

    public void signInClicked(View view)
    {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        boolean cond = true;
        if (username.equals(""))
        {
            shakeAndVibrate(mUsernameEditText);
            cond = false;
        }
        if (password.equals(""))
        {
            shakeAndVibrate(mPasswordEditText);
            cond = false;
        }
        if (cond)
        {
            password = Utilities.digest(password);
            mUserViewModel.getUser(username, password, this);
        }
    }

    private void shakeAndVibrate(EditText edittext)
    {
        edittext.startAnimation(mShakeAnimation);
        mVibrator.vibrate(VIBRATION_PERIOD);
    }


    @Override
    public void userReturned(User user)
    {
        if (user != null)
        {
            Utilities.setManager(user.isManager());
            Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else
        {
            Utilities.showAlertDialog(R.string.credentials_error_title, R.string.credentials_error_message, this);
        }
    }

    public void backButtonPressed(View view)
    {
        Intent intent = new Intent(SignInActivity.this, StartUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        backButtonPressed(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText)
            {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                if (!r.contains(rawX, rawY))
                {
                    view.clearFocus();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}

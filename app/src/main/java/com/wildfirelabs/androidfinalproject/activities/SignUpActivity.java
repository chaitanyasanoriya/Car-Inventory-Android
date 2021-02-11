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
import com.wildfirelabs.androidfinalproject.callbackinterfaces.UserSignUpCallbacks;
import com.wildfirelabs.androidfinalproject.viewmodels.UserViewModel;
import com.wildfirelabs.androidfinalproject.Utilities;
import com.wildfirelabs.androidfinalproject.models.User;

public class SignUpActivity extends AppCompatActivity implements UserSignUpCallbacks
{
    private EditText mNameEditText, mUsernameEditText, mPasswordEditText, mRetypePasswordEditText;
    private Animation mShakeAnimation;
    private Vibrator mVibrator;
    private final int VIBRATION_PERIOD = 500;
    private User mUser;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mNameEditText = (EditText) findViewById(R.id.full_name_edittext);
        mUsernameEditText = (EditText) findViewById(R.id.username_edittext);
        mPasswordEditText = (EditText) findViewById(R.id.password_edittext);
        mRetypePasswordEditText = (EditText) findViewById(R.id.retype_edittext);
        mShakeAnimation = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.shake);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        createManager();
    }

    private void createManager()
    {
        User user = new User("Manager","manager", Utilities.digest("test"),true);
        mUserViewModel.insert(user,null);
    }

    public void signUpClicked(View view)
    {
        String name = mNameEditText.getText().toString();
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String retype = mRetypePasswordEditText.getText().toString();
        boolean cond = true;
        if (name.equals(""))
        {
            shakeAndVibrate(mNameEditText);
            cond = false;
        }
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
        if (retype.equals(""))
        {
            shakeAndVibrate(mRetypePasswordEditText);
            cond = false;
        }
        if (cond)
        {
            if (password.equals(retype))
            {
                password = Utilities.digest(password);
                mUser = new User(name, username,password,false);
                mUserViewModel.getUsernameNumber(username,this);
            } else
            {
                shakeAndVibrate(mPasswordEditText);
                shakeAndVibrate(mRetypePasswordEditText);
            }
        }
    }

    private void shakeAndVibrate(EditText edittext)
    {
        edittext.startAnimation(mShakeAnimation);
        mVibrator.vibrate(VIBRATION_PERIOD);
    }

    @Override
    public void userSaved(long saved)
    {
        if (saved > 0)
        {
            Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Utilities.showAlertDialog(R.string.error, R.string.sign_up_error,this);
        }
    }

    @Override
    public void numberOfUsers(int number)
    {
        if(number > 0)
        {
            Utilities.showAlertDialog(R.string.username_used_title,R.string.username_used_message, this);
        }
        else
        {
            if(mUser!=null)
            {
                mUserViewModel.insert(mUser, this);
            }
        }
    }

    public void backButtonPressed(View view)
    {
        Intent intent = new Intent(SignUpActivity.this,StartUpActivity.class);
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

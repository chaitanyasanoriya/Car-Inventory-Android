package com.wildfirelabs.androidfinalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.wildfirelabs.androidfinalproject.R;

public class MainActivity extends AppCompatActivity
{
    private ConstraintLayout mConstrainLayout;
    private Animation mFadeInAnimation;
    private Animation mFadeOutAnimation;
    private final int WAIT_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //byPass();
        mConstrainLayout = (ConstraintLayout) findViewById(R.id.constrain_layout);
        mFadeInAnimation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this,R.anim.fade_out);
        mFadeInAnimation.setAnimationListener(mFadeInAnimationListener);
        mFadeOutAnimation.setAnimationListener(mFadeOutAnimationListener);
        mConstrainLayout.startAnimation(mFadeInAnimation);
    }

    private void byPass()
    {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private Animation.AnimationListener mFadeInAnimationListener = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation)
        {

        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            new Handler().postDelayed(() -> mConstrainLayout.startAnimation(mFadeOutAnimation),WAIT_TIME);
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {

        }
    };

    private Animation.AnimationListener mFadeOutAnimationListener = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation)
        {

        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            mConstrainLayout.setVisibility(View.GONE);
            Intent intent = new Intent(MainActivity.this, StartUpActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {

        }
    };
}

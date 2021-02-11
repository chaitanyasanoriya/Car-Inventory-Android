package com.wildfirelabs.androidfinalproject.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wildfirelabs.androidfinalproject.R;

public class StartUpActivity extends AppCompatActivity
{

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        askForPermissions();
    }

    private void askForPermissions()
    {
        if (ContextCompat.checkSelfPermission(StartUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(StartUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartUpActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(StartUpActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else
            {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(StartUpActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else
        {
            // Permission has already been granted
        }
    }

    public void signUp(View view)
    {
        Intent intent = new Intent(StartUpActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void signIn(View view)
    {
        Intent intent = new Intent(StartUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}

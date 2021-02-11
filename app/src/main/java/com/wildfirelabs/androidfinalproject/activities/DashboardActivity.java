package com.wildfirelabs.androidfinalproject.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wildfirelabs.androidfinalproject.adadpters.CarsRecyclerViewAdapter;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.CarsSearchCallback;
import com.wildfirelabs.androidfinalproject.viewmodels.CarsViewModel;
import com.wildfirelabs.androidfinalproject.R;
import com.wildfirelabs.androidfinalproject.Utilities;
import com.wildfirelabs.androidfinalproject.models.Cars;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements CarsSearchCallback
{
    private CarsViewModel carsViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Cars> mCars = null;
    private SearchView mSearchView;
    private CarsRecyclerViewAdapter mCarsRecyclerViewAdapter;
    private Button mAddCarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAddCarButton = findViewById(R.id.add_car_button);
        carsViewModel = new ViewModelProvider(this).get(CarsViewModel.class);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(mOueryTextListener);
        //addTwoCars();
        mCarsRecyclerViewAdapter = new CarsRecyclerViewAdapter(mCars, this);
        mRecyclerView.setAdapter(mCarsRecyclerViewAdapter);
        carsViewModel.getAllCars().observe(this, cars -> mCarsRecyclerViewAdapter.setCars(cars));
        Utilities.setCar(null);
    }

    private void addTwoCars()
    {
        File file = new File("/storage/emulated/0/Download/mustanggt.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Cars cars = new Cars("Ford Mustang GT", "Top Tier", 2020, (float) 600000, "Red", "MUSTANGGT", stream.toByteArray());
        carsViewModel.insert(cars, null);
    }

    SearchView.OnQueryTextListener mOueryTextListener = new SearchView.OnQueryTextListener()
    {
        @Override
        public boolean onQueryTextSubmit(String s)
        {
            if (s.equals(""))
            {
                mCarsRecyclerViewAdapter.setCars(carsViewModel.getAllCars().getValue());
            } else
            {
                carsViewModel.searchCar("%" + s + "%", DashboardActivity.this);
            }
            mSearchView.clearFocus();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s)
        {
            carsViewModel.searchCar("%" + s + "%", DashboardActivity.this);
            return false;
        }
    };

    @Override
    public void searchCarsResult(List<Cars> cars)
    {
        System.out.println(cars);
        mCarsRecyclerViewAdapter.setCars(cars);
    }

    public void addCarClicked(View view)
    {
        Intent intent = new Intent(DashboardActivity.this, DetailsActivity.class);
        Utilities.setCar(null);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(!Utilities.isManager())
        {
            mAddCarButton.setVisibility(View.GONE);
        }
    }
}

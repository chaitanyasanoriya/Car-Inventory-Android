package com.wildfirelabs.androidfinalproject.adadpters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.card.MaterialCardView;
import com.wildfirelabs.androidfinalproject.R;
import com.wildfirelabs.androidfinalproject.Utilities;
import com.wildfirelabs.androidfinalproject.activities.DashboardActivity;
import com.wildfirelabs.androidfinalproject.activities.DetailsActivity;
import com.wildfirelabs.androidfinalproject.models.Cars;

import java.util.List;

public class CarsRecyclerViewAdapter extends RecyclerView.Adapter<CarsRecyclerViewAdapter.ViewHolder>
{
    private List<Cars> mCars;
    private Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mNameTextView;
        public TextView mPriceTextView;
        public ImageView mImageView;
        public FrameLayout mNameFrameLayout;
        public FrameLayout mPriceFrameLayout;
        public MaterialCardView mBlurredView;
        public ConstraintLayout mDragView;
        public RealtimeBlurView mHandleAreaBlurView;
        public LinearLayout mLinearLayout;
        public TextView mDescNameTextView;
        public TextView mDescModelTextView;
        public TextView mDescColorTextView;
        public TextView mDescVinTextView;
        public TextView mDescYearTextView;
        public TextView mDescPriceTextView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.mNameTextView = itemView.findViewById(R.id.car_name_textview);
            this.mPriceTextView = itemView.findViewById(R.id.price_textview);
            this.mImageView = itemView.findViewById(R.id.image_view);
            this.mNameFrameLayout = itemView.findViewById(R.id.name_frameLayout);
            this.mPriceFrameLayout = itemView.findViewById(R.id.price_frameLayout);
            this.mBlurredView = itemView.findViewById(R.id.blurred_view);
            mHandleAreaBlurView = itemView.findViewById(R.id.blur_view);
            mDragView = itemView.findViewById(R.id.dragview);
            mLinearLayout = itemView.findViewById(R.id.linearlayout);
            mDescNameTextView = itemView.findViewById(R.id.name_textview);
            mDescModelTextView = itemView.findViewById(R.id.model_textview);
            mDescColorTextView = itemView.findViewById(R.id.color_textview);
            mDescVinTextView = itemView.findViewById(R.id.vin_textview);
            mDescYearTextView = itemView.findViewById(R.id.year_textview);
            mDescPriceTextView = itemView.findViewById(R.id.desc_price_textview);
        }
    }

    public CarsRecyclerViewAdapter(List<Cars> mCars, Context context)
    {
        this.mCars = mCars;
        this.mContext = context;
    }

    public List<Cars> getCars()
    {
        return mCars;
    }

    public void setCars(List<Cars> mCars)
    {
        this.mCars = mCars;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.car_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarsRecyclerViewAdapter.ViewHolder holder, int position)
    {
        if (mCars != null && mCars.size() > 0)
        {
            final Cars car = mCars.get(position);
            holder.mNameTextView.setText(car.getName());
            if (car.getImage() != null)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
                holder.mImageView.setImageBitmap(bitmap);
            }
            holder.mPriceTextView.setText("$ " + car.getPrice());
            holder.mNameFrameLayout.setClipToOutline(true);
            holder.mPriceFrameLayout.setClipToOutline(true);
            String[] descriptions = null;
            if (car != null)
            {
                descriptions = getDescriptions(car);
            }
            setData(descriptions, holder.mDescNameTextView, holder.mDescModelTextView, holder.mDescColorTextView, holder.mDescVinTextView, holder.mDescYearTextView, holder.mDescPriceTextView);
            final boolean[] isExpanded = {false};
            final float original = holder.mDragView.getTranslationY();
            holder.mHandleAreaBlurView.setOnClickListener(view ->
            {
                if (isExpanded[0])
                {
                    holder.mDragView.animate().translationY(original);
                    isExpanded[0] = false;
                } else
                {
                    holder.mDragView.animate().translationY(-(holder.mDragView.getMeasuredHeight()));
                    isExpanded[0] = true;
                }
            });
            holder.mImageView.setOnClickListener(view ->
            {
                Intent intent = new Intent(((DashboardActivity) mContext), DetailsActivity.class);
                Utilities.setCar(car);
                String transitionName = mContext.getString(R.string.transition_string);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, (View) holder.mBlurredView, transitionName);
                ActivityCompat.startActivity(mContext, intent, options.toBundle());
            });
        } else
        {
            setPlaceHolderCar(holder);
        }
    }

    private void setPlaceHolderCar(ViewHolder holder)
    {
        holder.mNameTextView.setText(R.string.no_car);
        holder.mPriceTextView.setText(R.string.no_car_price);
        holder.mImageView.setImageResource(R.mipmap.placeholder);
        holder.mDescNameTextView.setText("");
        holder.mDescModelTextView.setText("");
        holder.mDescColorTextView.setText("");
        holder.mDescVinTextView.setText("");
        holder.mDescPriceTextView.setText("");
        holder.mDescYearTextView.setText("");
    }

    private void setData(String[] descriptions, TextView mDescNameTextView, TextView mDescModelTextView, TextView mDescColorTextView, TextView mDescVinTextView, TextView mDescYearTextView, TextView mDescPriceTextView)
    {
        if (descriptions != null)
        {
            mDescNameTextView.setText(descriptions[0]);
            mDescModelTextView.setText(descriptions[1]);
            mDescColorTextView.setText(descriptions[2]);
            mDescVinTextView.setText(descriptions[3]);
            mDescYearTextView.setText(descriptions[4]);
            mDescPriceTextView.setText(descriptions[5]);
        } else
        {
            mDescNameTextView.setVisibility(View.INVISIBLE);
            mDescModelTextView.setVisibility(View.INVISIBLE);
            mDescColorTextView.setVisibility(View.INVISIBLE);
            mDescVinTextView.setVisibility(View.INVISIBLE);
            mDescYearTextView.setVisibility(View.INVISIBLE);
            mDescPriceTextView.setVisibility(View.INVISIBLE);
        }
    }

    private String[] getDescriptions(Cars car)
    {
        String[] descriptions = new String[6];
        descriptions[0] = car.getName();
        descriptions[1] = car.getModel();
        descriptions[2] = car.getColor();
        descriptions[3] = car.getVin();
        descriptions[4] = String.valueOf(car.getYear());
        descriptions[5] = String.valueOf(car.getPrice());
        return descriptions;
    }

    @Override
    public int getItemCount()
    {
        if (mCars != null && mCars.size() > 0)
        {
            return mCars.size();
        } else
        {
            return 1;
        }
    }

}

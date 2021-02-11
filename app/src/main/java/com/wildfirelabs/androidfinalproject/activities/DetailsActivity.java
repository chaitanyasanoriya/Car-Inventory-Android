package com.wildfirelabs.androidfinalproject.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.mmin18.widget.RealtimeBlurView;
import com.rw.keyboardlistener.KeyboardUtils;
import com.wildfirelabs.androidfinalproject.callbackinterfaces.CarsCUDCallbacks;
import com.wildfirelabs.androidfinalproject.viewmodels.CarsViewModel;
import com.wildfirelabs.androidfinalproject.R;
import com.wildfirelabs.androidfinalproject.Utilities;
import com.wildfirelabs.androidfinalproject.models.Cars;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity implements CarsCUDCallbacks
{
    private static final int CAMERA_REQUEST_CODE = 121;
    private static final int GALLERY_REQUEST_CODE = 2404;
    private static final long VIBRATION_PERIOD = 500;
    private Cars mCar = null;
    private ImageView mImageView;
    private EditText mNameEditText, mModelEditText, mColorEditText, mVinEditText, mYearEditText, mPriceEditText;
    private LinearLayout mDragView;
    private RealtimeBlurView mHandlerBlurView;
    private boolean isExpanded = false;
    private float mOriginalPlace;
    private FrameLayout mCameraFrameLayout, mDoneFrameLayout;
    private final int CAMERA_PERM_CODE = 101;
    private Bitmap mSelectedImage = null;
    private Animation mShakeAnimation;
    private Vibrator mVibrator;
    private CarsViewModel mCarsViewModel;
    private Button mDeleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mCar = Utilities.getCar();
    }

    private void setMembers()
    {
        mImageView = findViewById(R.id.image_view);
        mNameEditText = findViewById(R.id.name_edittext);
        mModelEditText = findViewById(R.id.model_edittext);
        mColorEditText = findViewById(R.id.color_edittext);
        mVinEditText = findViewById(R.id.vin_edittext);
        mYearEditText = findViewById(R.id.year_edittext);
        mPriceEditText = findViewById(R.id.desc_price_edittext);
        mDragView = findViewById(R.id.dragview);
        mHandlerBlurView = findViewById(R.id.blur_view);
        mCameraFrameLayout = findViewById(R.id.camera_fram_layout);
        mDoneFrameLayout = findViewById(R.id.done_fram_layout);
        mDeleteButton = findViewById(R.id.delete_button);
        mCameraFrameLayout.setClipToOutline(true);
        mDoneFrameLayout.setClipToOutline(true);
        mHandlerBlurView.setOnClickListener(mOnClickListener);
        mShakeAnimation = AnimationUtils.loadAnimation(DetailsActivity.this, R.anim.shake);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mCarsViewModel = new ViewModelProvider(this).get(CarsViewModel.class);
        findViewById(R.id.linearlayout).requestFocus();
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                Log.d("keyboard", "keyboard visible: " + isVisible);
                if (isExpanded && !isVisible)
                {
                    mDragView.animate().translationY(mOriginalPlace);
                }
            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        setMembers();
        if (mCar != null)
        {
            if (mCar.getImage() != null)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(mCar.getImage(), 0, mCar.getImage().length);
                mImageView.setImageBitmap(bitmap);
            }
            mNameEditText.setText(mCar.getName());
            mModelEditText.setText(mCar.getModel());
            mColorEditText.setText(mCar.getColor());
            mVinEditText.setText(mCar.getVin());
            mVinEditText.setEnabled(false);
            mYearEditText.setText(String.valueOf(mCar.getYear()));
            mPriceEditText.setText(String.valueOf(mCar.getPrice()));
        }
        if (!Utilities.isManager())
        {
            mNameEditText.setEnabled(false);
            mModelEditText.setEnabled(false);
            mColorEditText.setEnabled(false);
            mVinEditText.setEnabled(false);
            mYearEditText.setEnabled(false);
            mPriceEditText.setEnabled(false);
            mCameraFrameLayout.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.GONE);

        }
        mNameEditText.setOnFocusChangeListener(mOnFocusListener);
        mModelEditText.setOnFocusChangeListener(mOnFocusListener);
        mColorEditText.setOnFocusChangeListener(mOnFocusListener);
        mVinEditText.setOnFocusChangeListener(mOnFocusListener);
        mYearEditText.setOnFocusChangeListener(mOnFocusListener);
        mPriceEditText.setOnFocusChangeListener(mOnFocusListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if (isExpanded)
            {
                mDragView.animate().translationY(mOriginalPlace);
                isExpanded = false;
            } else
            {
                expandDragView();
            }
        }
    };

    private void expandDragView()
    {
        mOriginalPlace = mDragView.getTranslationY();
        mDragView.animate().translationY(-(mDragView.getMeasuredHeight()));
        isExpanded = true;
    }

    View.OnFocusChangeListener mOnFocusListener = (view, b) ->
    {
        if (b && !isExpanded)
        {
            expandDragView();
        }
        if (b)
        {
            float diff = -(view.getY() + mDragView.getMeasuredHeight());
            mDragView.animate().translationY(diff);
        }
    };

    public void cameraClicked(View view)
    {
        String[] arr = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Choose Image From: ");
        dialog.setItems(arr, (dialog1, position) ->
        {
            if (position == 0)
            {
                askCameraPermissions();
            } else
            {
                ImagePicker.Companion.with(this)
                        .galleryOnly()
                        .compress(1024)
                        .start();
            }
        });
        dialog.setPositiveButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alert = dialog.create();
        alert.show();

    }

    private void askCameraPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
        } else
        {
            openCamera();
            //dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == CAMERA_PERM_CODE)
        {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                openCamera();
            } else
            {
                Toast.makeText(this, "Camera and File Access Permission is Required to Use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera()
    {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                mSelectedImage = bitmap;
                mImageView.setImageBitmap(bitmap);
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK && data != null && data.getData() != null)
            {
                File file = ImagePicker.Companion.getFile(data);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                mSelectedImage = bitmap;
                mImageView.setImageBitmap(bitmap);
            }
        }
    }


    public void doneClicked(View view)
    {
        if (Utilities.isManager())
        {
            boolean isEverythingOkay = true;
            String name = mNameEditText.getText().toString();
            String model = mModelEditText.getText().toString();
            String color = mColorEditText.getText().toString();
            String vin = mVinEditText.getText().toString();
            String year = mYearEditText.getText().toString();
            String price = mPriceEditText.getText().toString();
            int year_int = 0;
            float price_float = 0f;
            if (name.equals(""))
            {
                shakeAndVibrate(mNameEditText);
                isEverythingOkay = false;
            }
            if (model.equals(""))
            {
                shakeAndVibrate(mModelEditText);
                isEverythingOkay = false;
            }
            if (color.equals(""))
            {
                shakeAndVibrate(mColorEditText);
                isEverythingOkay = false;
            }
            if (vin.equals(""))
            {
                shakeAndVibrate(mVinEditText);
                isEverythingOkay = false;
            }
            if (year.equals(""))
            {
                shakeAndVibrate(mYearEditText);
                isEverythingOkay = false;
            } else
            {
                try
                {
                    year_int = Integer.valueOf(year);
                } catch (Exception e)
                {
                    shakeAndVibrate(mYearEditText);
                    isEverythingOkay = false;
                }
            }
            if (price.equals(""))
            {
                shakeAndVibrate(mPriceEditText);
                isEverythingOkay = false;
            } else
            {
                try
                {
                    price_float = Float.valueOf(price);
                } catch (Exception e)
                {
                    shakeAndVibrate(mPriceEditText);
                    isEverythingOkay = false;
                }
            }
            if (isEverythingOkay)
            {
                if (mCar == null)
                {
                    Cars car;
                    if (mSelectedImage == null)
                    {
                        car = new Cars(name, model, year_int, price_float, color, vin, null);
                    } else
                    {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        mSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        car = new Cars(name, model, year_int, price_float, color, vin, stream.toByteArray());
                    }
                    mCarsViewModel.insert(car, this);
                } else
                {
                    mCar.setName(name);
                    mCar.setModel(model);
                    mCar.setColor(color);
                    mCar.setYear(year_int);
                    mCar.setPrice(price_float);
                    if (mSelectedImage != null)
                    {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        mSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        mCar.setImage(stream.toByteArray());
                    }
                    mCarsViewModel.update(mCar, this);
                }
            }
        } else
        {
            finish();
        }
    }

    private void shakeAndVibrate(EditText edittext)
    {
        edittext.startAnimation(mShakeAnimation);
        mVibrator.vibrate(VIBRATION_PERIOD);
    }

    @Override
    public void insertedResult(long nrows)
    {
        if (nrows > 0)
        {
            finish();
        } else
        {
            Utilities.showAlertDialog(R.string.insert_error_title, R.string.insert_error_message, this);
        }
    }

    @Override
    public void updatedResult(int nrows)
    {
        if (nrows > 0)
        {
            finish();
        } else
        {
            Utilities.showAlertDialog(R.string.update_error_title, R.string.update_error_message, this);
        }
    }

    @Override
    public void deletedResult(int nrows)
    {
        if (nrows > 0)
        {
            finish();
        } else
        {
            Utilities.showAlertDialog(R.string.delete_error_title, R.string.delete_error_message, this);
        }
    }

    public void deleteClicked(View view)
    {
        mCarsViewModel.delete(mCar, this);
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
                mDragView.animate().translationY(-(mDragView.getMeasuredHeight()));
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    String currentPhotoPath;

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            } catch (IOException ex)
            {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.wildfirelabs.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

}

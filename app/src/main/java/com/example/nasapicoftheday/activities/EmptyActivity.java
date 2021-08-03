package com.example.nasapicoftheday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nasapicoftheday.R;
import com.example.nasapicoftheday.fragments.ViewImage;

/**
 * The EmptyActivity class is used to hold the View Image Fragment.
 *
 * @author Caitlin Ross
 */
public class EmptyActivity extends AppCompatActivity {

    /**
     * Creates the Empty Activity and attaches the View Image fragment.
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        ViewImage viewImage = new ViewImage();
        viewImage.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.view_image_frame, viewImage).commit();
    }
}
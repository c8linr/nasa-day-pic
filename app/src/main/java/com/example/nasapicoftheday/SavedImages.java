package com.example.nasapicoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * The SavedImages class contains the functionality for the Saved Images activity
 */
public class SavedImages extends AppCompatActivity {
    ArrayList<Image> imageList;

    /**
     * Creates the Saved Images activity and add the functionality
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        // Initialize the array list of images
        imageList = new ArrayList<Image>();
    }
}
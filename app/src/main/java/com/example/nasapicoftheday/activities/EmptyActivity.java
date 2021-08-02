package com.example.nasapicoftheday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nasapicoftheday.R;
import com.example.nasapicoftheday.fragments.ViewImage;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        ViewImage viewImage = new ViewImage();
        viewImage.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.view_image_frame, viewImage).commit();
    }
}
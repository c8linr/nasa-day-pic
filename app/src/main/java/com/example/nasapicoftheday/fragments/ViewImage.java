package com.example.nasapicoftheday.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nasapicoftheday.R;

/**
 * A Fragment to view the image selected in the Saved Images activity.
 *
 */
public class ViewImage extends Fragment {
    AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Retrieve the image data
        Bundle imageData = getArguments();

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_image, container, false);

        // Initialize the widgets

        return result;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
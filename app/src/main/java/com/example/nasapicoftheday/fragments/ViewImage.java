package com.example.nasapicoftheday.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nasapicoftheday.R;
import com.example.nasapicoftheday.activities.SavedImages;
import com.example.nasapicoftheday.dao.ImageDao;
import com.example.nasapicoftheday.datamodel.Image;
import com.google.android.material.snackbar.Snackbar;

/**
 * A Fragment to view the image selected in the Saved Images activity.
 *
 */
public class ViewImage extends Fragment {
    AppCompatActivity parentActivity;

    public ViewImage() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Retrieve the image data and create an Image object
        if(getArguments() == null) {
            Intent backToSavedImages = new Intent(parentActivity.getBaseContext(), SavedImages.class);
            parentActivity.startActivity(backToSavedImages);
        }
        Image imageObject = new Image(getArguments());

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_image, container, false);

        // Initialize the widgets
        ImageView imageView = result.findViewById(R.id.fragment_view_image);
        imageView.setImageBitmap(imageObject.loadImage(parentActivity));

        TextView imageName = result.findViewById(R.id.fragment_image_name);
        imageName.setText(imageObject.getName());

        Button editButton = result.findViewById(R.id.fragment_edit_button);
        // TODO: some kind of dialog to edit the name

        Button deleteButton = result.findViewById(R.id.fragment_delete_button);
        deleteButton.setOnClickListener( (click) -> {
            ImageDao dao = new ImageDao();
            dao.deleteImage(imageObject, parentActivity);
            Snackbar.make(imageView, R.string.fragment_delete_msg, Snackbar.LENGTH_LONG)
                    .setAction(R.string.fragment_undo_delete, (c) -> dao.saveImage(imageObject, parentActivity)).show();
            });

        return result;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
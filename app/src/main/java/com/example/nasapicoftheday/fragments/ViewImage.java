package com.example.nasapicoftheday.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * @author Caitlin Ross
 */
public class ViewImage extends Fragment {
    private AppCompatActivity parentActivity;

    public static final String IMAGE_KEY = "com.example.nasapicoftheday.DeletedImage";

    /**
     * Creates the View Image Fragment and adds the functionality.
     *
     * @param inflater the object that will inflate the Fragment's layout
     * @param container the ViewGroup that will contain the Fragment
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     * @return the inflated View representing the Fragment
     */
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
        editButton.setOnClickListener( (click) -> {
            EditText newNameField = new EditText(parentActivity);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parentActivity);
            alertDialogBuilder.setTitle(R.string.fragment_edit_dialog_title);
            alertDialogBuilder.setMessage(R.string.fragment_edit_dialog_message);
            alertDialogBuilder.setPositiveButton(R.string.fragment_edit_dialog_yes, (c, arg) -> {
                String newName = newNameField.getText().toString();
                ImageDao dao = new ImageDao();
                dao.updateImage(imageObject, newName, parentActivity);
                imageName.setText(newName);
            });
            alertDialogBuilder.setNegativeButton(R.string.fragment_edit_dialog_no, (c, arg) -> { });
            alertDialogBuilder.setView(newNameField);
            alertDialogBuilder.create().show();
        });

        Button deleteButton = result.findViewById(R.id.fragment_delete_button);
        deleteButton.setOnClickListener( (click) -> {
            // Delete the image
            ImageDao dao = new ImageDao();
            dao.deleteImage(imageObject, parentActivity);
            // Go back to the Saved Images activity
            Intent backToSavedImages = new Intent(parentActivity.getBaseContext(), SavedImages.class);
            backToSavedImages.putExtra(IMAGE_KEY, imageObject.getBundle());
            parentActivity.startActivity(backToSavedImages);
            });

        return result;
    }

    /**
     * Sets the context for the fragment when it is attached.
     *
     * @param context the context that called this method
     */
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
package com.example.nasapicoftheday.menus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nasapicoftheday.R;
import com.example.nasapicoftheday.activities.MainActivity;
import com.example.nasapicoftheday.activities.NewImage;
import com.example.nasapicoftheday.activities.SavedImages;

/**
 * The Toolbar class is used to keep the navigation logic in one place.
 *
 * @author Caitlin Ross
 */
public class Toolbar {
    /**
     * Directs the user to the appropriate activity.
     *
     * @param item the menu item that was clicked
     * @param parentActivity the activity calling this method
     * @param calledBy the Activity enum matching the calling activity
     */
    @SuppressLint("NonConstantResourceId")
    public static void navigate(MenuItem item, AppCompatActivity parentActivity, Activity calledBy) {
        switch (item.getItemId()) {
            case R.id.menu_welcome:
                Intent goToWelcome = new Intent(parentActivity, MainActivity.class);
                parentActivity.startActivity(goToWelcome);
                break;
            case R.id.menu_help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parentActivity);
                alertDialogBuilder.setTitle(calledBy.getHelpTitle(parentActivity));
                alertDialogBuilder.setMessage(calledBy.getHelpMessage(parentActivity));
                alertDialogBuilder.setNeutralButton(R.string.help_close, (click, arg) -> { });
                alertDialogBuilder.create().show();
                break;
            case R.id.menu_new_image:
                Intent goToNewImage = new Intent(parentActivity, NewImage.class);
                parentActivity.startActivity(goToNewImage);
                break;
            case R.id.menu_saved_images:
                Intent goToSavedImages = new Intent(parentActivity, SavedImages.class);
                parentActivity.startActivity(goToSavedImages);
                break;
        }
    }
}

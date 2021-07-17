package com.example.nasapicoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * The MainActivity class contains the functionality for the welcome page.
 *
 * @author Caitlin Ross
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The onCreate method creates the Main Activity and adds the functionality.
     *
     * @param savedInstanceState a Bundle passed in when MainActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pressing the "new image" button takes the user to the New Image activity
        Button newImageButton = findViewById(R.id.welcome_new_button);
        Intent goToNewImage = new Intent(this, NewImage.class);
        newImageButton.setOnClickListener( (click) -> startActivity(goToNewImage));

        // Pressing the "saved images" button takes the user to the Saved Images activity
        Button savedImagesButton = findViewById(R.id.welcome_saved_button);
        Intent goToSavedImages = new Intent(this, SavedImages.class);
        savedImagesButton.setOnClickListener( (click) -> startActivity(goToSavedImages));
    }
}
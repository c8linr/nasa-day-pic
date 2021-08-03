package com.example.nasapicoftheday.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nasapicoftheday.R;
import com.example.nasapicoftheday.menus.Activity;
import com.example.nasapicoftheday.menus.NavigationDrawer;
import com.google.android.material.navigation.NavigationView;

/**
 * The MainActivity class contains the functionality for the welcome page.
 *
 * @author Caitlin Ross
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /** Static constants for the Saved Preferences file name and key(s) */
    private static final String PREFS_FILE = "NasaPicPrefs";
    private static final String NAME_KEY = "UserName";

    /**
     * Creates the Main Activity and adds the functionality.
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the saved preferences file and check for a saved name
        SharedPreferences getPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        String savedName = getPreferences.getString(NAME_KEY, "");

        // Set up the personalized welcome
        EditText personalWelcome = findViewById(R.id.welcome_personal);
        Button saveNameButton = findViewById(R.id.welcome_save_name);

        // If there is a saved name, show the personalized welcome
        if(!savedName.isEmpty()){
            showPersonalizedWelcome(savedName);
        } // Otherwise, allow the user to input their name
        else {
            personalWelcome.setEnabled(true);
            personalWelcome.setHint(R.string.welcome_name_prompt);
            saveNameButton.setVisibility(View.VISIBLE);
        }

        // Add listener to the save name button
        saveNameButton.setOnClickListener( (click) -> {
            // Extract the name from the widget
            String name = personalWelcome.getText().toString();

            // Save the name to file
            SharedPreferences savePreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = savePreferences.edit();
            editor.putString(NAME_KEY, name);
            editor.apply();

            // Update the GUI accordingly
            showPersonalizedWelcome(name);
        });

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the navigation drawer
        NavigationDrawer.init(this, this, toolbar, Activity.MAIN);

        // Pressing the "new image" button takes the user to the New Image activity
        Button newImageButton = findViewById(R.id.welcome_new_button);
        Intent goToNewImage = new Intent(this, NewImage.class);
        newImageButton.setOnClickListener( (click) -> startActivity(goToNewImage));

        // Pressing the "saved images" button takes the user to the Saved Images activity
        Button savedImagesButton = findViewById(R.id.welcome_saved_button);
        Intent goToSavedImages = new Intent(this, SavedImages.class);
        savedImagesButton.setOnClickListener( (click) -> startActivity(goToSavedImages));
    }

    /**
     * Displays the personalized welcome and adjusts the GUI accordingly.
     *
     * @param name the name of the user to be used in the welcome message
     */
    private void showPersonalizedWelcome(String name) {
        // Get references to the widgets
        EditText personalWelcome = findViewById(R.id.welcome_personal);
        Button saveNameButton = findViewById(R.id.welcome_save_name);

        // Build the personalized message
        String personalizedMessage = getString(R.string.welcome_back) + " " + name + "!";

        // Disable editing, display the message, and remove the button
        personalWelcome.setEnabled(false);
        personalWelcome.setText(personalizedMessage);
        personalWelcome.setTextColor(getResources().getColor(R.color.design_default_color_on_secondary, null));
        saveNameButton.setVisibility(View.GONE);
    }

    /**
     * Inflates the toolbar's layout.
     *
     * @param m the menu being created
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, m);
        return true;
    }

    /**
     * Delegates the navigation logic to the local Toolbar class.
     *
     * @param item the menu item selected
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        com.example.nasapicoftheday.menus.Toolbar.navigate(item, this, Activity.MAIN);
        return true;
    }

    /**
     * Delegates the navigation logic to the NavigationDrawer class.
     *
     * @param item the menu item selected
     * @return false
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavigationDrawer.navigate(item, this, Activity.MAIN);
        return false;
    }
}
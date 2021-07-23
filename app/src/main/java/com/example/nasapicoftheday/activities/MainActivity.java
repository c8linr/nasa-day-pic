package com.example.nasapicoftheday.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.example.nasapicoftheday.R;
import com.google.android.material.navigation.NavigationView;

/**
 * The MainActivity class contains the functionality for the welcome page.
 *
 * @author Caitlin Ross
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Creates the Main Activity and adds the functionality.
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the navigation drawer
        DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open_desc, R.string.drawer_close_desc);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(this);

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
     * Directs the user to the correct activity when an option from the toolbar is selected.
     *
     * @param item the menu item selected
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_welcome:
                Intent goToWelcome = new Intent(this, MainActivity.class);
                startActivity(goToWelcome);
                break;
            case R.id.menu_help:
                // show help dialog
                break;
            case R.id.menu_new_image:
                Intent goToNewImage = new Intent(this, NewImage.class);
                startActivity(goToNewImage);
                break;
            case R.id.menu_saved_images:
                Intent goToSavedImages = new Intent(this, SavedImages.class);
                startActivity(goToSavedImages);
                break;
        }
        return true;
    }

    /**
     * Directs the user to the correct activity when an option from the navigation drawer is selected.
     *
     * @param item the menu item selected
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_welcome_button:
                Intent goToWelcome = new Intent(this, MainActivity.class);
                startActivity(goToWelcome);
                break;
            case R.id.drawer_new_image_button:
                Intent goToNewImage = new Intent(this, NewImage.class);
                startActivity(goToNewImage);
                break;
            case R.id.drawer_saved_images_button:
                Intent goToSavedImages = new Intent(this, SavedImages.class);
                startActivity(goToSavedImages);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
}
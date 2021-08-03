package com.example.nasapicoftheday.menus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.nasapicoftheday.R;
import com.example.nasapicoftheday.activities.MainActivity;
import com.example.nasapicoftheday.activities.NewImage;
import com.example.nasapicoftheday.activities.SavedImages;
import com.google.android.material.navigation.NavigationView;

/**
 * The NavigationDrawer class contains the logic for the navigation drawer.
 *
 * @author Caitlin Ross
 */
public class NavigationDrawer {
    /**
     * Directs the user to the correct activity when an option from the navigation drawer is selected.
     *
     * @param item the menu item selected
     * @param parentActivity a reference to the activity that called this method
     */
    @SuppressLint("NonConstantResourceId")
    public static void navigate(MenuItem item, AppCompatActivity parentActivity, Activity calledBy) {
        switch (item.getItemId()) {
            case R.id.drawer_welcome_button:
                Intent goToWelcome = new Intent(parentActivity, MainActivity.class);
                parentActivity.startActivity(goToWelcome);
                break;
            case R.id.drawer_new_image_button:
                Intent goToNewImage = new Intent(parentActivity, NewImage.class);
                parentActivity.startActivity(goToNewImage);
                break;
            case R.id.drawer_saved_images_button:
                Intent goToSavedImages = new Intent(parentActivity, SavedImages.class);
                parentActivity.startActivity(goToSavedImages);
                break;
        }

        DrawerLayout drawerLayout = calledBy.getLayout(parentActivity);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Sets up the navigation drawer based on the activity that called the method.
     *
     * @param parentActivity the AppCompatActivity that called the method
     * @param navListener the OnNavigationItemSelectedListener that called the method
     * @param toolbar the toolbar containing the drawer
     * @param calledBy the Activity enum matching the calling Activity
     */
    public static void init(AppCompatActivity parentActivity,
                            NavigationView.OnNavigationItemSelectedListener navListener,
                            androidx.appcompat.widget.Toolbar toolbar,
                            Activity calledBy) {

        DrawerLayout drawerLayout = calledBy.getLayout(parentActivity);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(parentActivity, drawerLayout,
                toolbar, R.string.drawer_open_desc, R.string.drawer_close_desc);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = parentActivity.findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(navListener);

        // Set the activity name in the navigation header
        View headerView = navigationView.getHeaderView(0);
        TextView activityName = headerView.findViewById(R.id.nav_header_activity_name);
        activityName.setText(calledBy.getActivityName(parentActivity));
    }
}
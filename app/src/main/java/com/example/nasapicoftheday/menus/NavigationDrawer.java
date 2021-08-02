package com.example.nasapicoftheday.menus;

import android.content.Intent;
import android.view.MenuItem;

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
 */
public class NavigationDrawer {
    /**
     * Directs the user to the correct activity when an option from the navigation drawer is selected.
     *
     * @param item the menu item selected
     * @param parentActivity a reference to the activity that called this method
     */
    public static void navigate(MenuItem item, AppCompatActivity parentActivity) {
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

        DrawerLayout drawerLayout = parentActivity.findViewById(R.id.main_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Sets up the navigation drawer based on the activity that called the method.
     *
     * @param parentActivity the AppCompatActivity that called the method
     * @param navListener the OnNavigationItemSelectedListener that called the method
     * @param toolbar the toolbar containing the drawer
     * @param calledBy the Activities enum matching the calling Activity
     */
    public static void setUp(AppCompatActivity parentActivity,
                             NavigationView.OnNavigationItemSelectedListener navListener,
                             androidx.appcompat.widget.Toolbar toolbar,
                             CallingActivity calledBy) {

        DrawerLayout drawerLayout = calledBy.getLayout(parentActivity);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(parentActivity, drawerLayout,
                toolbar, R.string.drawer_open_desc, R.string.drawer_close_desc);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = parentActivity.findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(navListener);
    }

    /**
     * The CallingActivity enum represents the possible activities that can use the navigation drawer
     */
    public enum CallingActivity {
        MAIN(R.id.main_drawer_layout),
        NEW(R.id.new_image_drawer_layout),
        DOWNLOAD(R.id.download_image_drawer_layout),
        SAVED(R.id.saved_images_drawer_layout);

        private final int drawerLayoutID;

        /**
         * Constructor, initializes the ID of the relevant DrawerLayout
         *
         * @param id the resource ID associated with the DrawerLayout that will be initialized
         */
        CallingActivity(int id) {
            drawerLayoutID = id;
        }

        // Accessor
        public DrawerLayout getLayout(AppCompatActivity parent) {
            return parent.findViewById(drawerLayoutID);
        }
    }
}
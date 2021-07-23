package com.example.nasapicoftheday.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nasapicoftheday.dao.ImageDao;
import com.example.nasapicoftheday.datamodel.Image;
import com.example.nasapicoftheday.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * The SavedImages class contains the functionality for the Saved Images activity
 */
public class SavedImages extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Image> imageList;
    ImageListAdapter adapter;

    /**
     * Creates the Saved Images activity and add the functionality
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the navigation drawer
        DrawerLayout drawerLayout = findViewById(R.id.saved_images_drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open_desc, R.string.drawer_close_desc);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Load the images from the database
        ImageDao dao = new ImageDao();
        imageList = new ArrayList<>(dao.loadImages(this));

        //Populate the ListView
        ListView imageListView = findViewById(R.id.view_image_list);
        adapter = new ImageListAdapter();
        imageListView.setAdapter(adapter);

        // Add a listener to the List View to load the fragment/empty activity when an image is clicked
        imageListView.setOnItemClickListener( (parent, view, pos, id) -> {
            Image selectedImage = imageList.get(pos);
            Intent viewImage = new Intent(SavedImages.this, EmptyActivity.class);
            startActivity(viewImage, selectedImage.getBundle());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload the images from the database
        ImageDao dao = new ImageDao();
        imageList = new ArrayList<>(dao.loadImages(this));

        // Notify the ListView that the data has updated
        adapter.notifyDataSetChanged();
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

        DrawerLayout drawerLayout = findViewById(R.id.saved_images_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    /**
     * The ImageListAdapter class is used to link the list of Image objects to the ListView widget
     */
    private class ImageListAdapter extends BaseAdapter {

        /**
         * Returns the number of items in the list.
         *
         * @return the number of items in the list
         */
        @Override
        public int getCount() {
            return imageList.size();
        }

        /**
         * Returns the Object at that position.
         *
         * @param position the position selected
         * @return the Object at that position
         */
        @Override
        public Object getItem(int position) {
            return imageList.get(position);
        }

        /**
         * Returns the ID of the item at a given position.
         *
         * @param position the position of the item
         * @return the ID of the item
         */
        @Override
        public long getItemId(int position) {
            return imageList.get(position).getId();
        }

        /**
         * Populates the list item.
         *
         * @param position the position of the item in the list
         * @param convertView the View object representing the list item
         * @param parent for getting the parent context
         * @return the populated view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Inflate the layout for the list item
            LayoutInflater inflater = getLayoutInflater();

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.image_list_item, parent, false);
            }

            // Get the Image object to be displayed
            Image i = imageList.get(position);

            // Set the thumbnail image
            ImageView thumbnail = convertView.findViewById(R.id.image_list_thumbnail);
            thumbnail.setImageBitmap(i.loadImage(parent.getContext()));

            // Set the image date (the day it was NASA's Image of the Day)
            TextView imageDate = convertView.findViewById(R.id.image_list_date);
            imageDate.setText(i.getImageDate().toString());

            // Set the image name (user-defined)
            TextView imageName = convertView.findViewById(R.id.image_list_name);
            imageName.setText(i.getName());

            return convertView;
        }
    }
}
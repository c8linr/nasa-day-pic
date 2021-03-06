package com.example.nasapicoftheday.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.nasapicoftheday.fragments.ViewImage;
import com.example.nasapicoftheday.menus.Activity;
import com.example.nasapicoftheday.menus.NavigationDrawer;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * The SavedImages class contains the functionality for the Saved Images activity.
 *
 * @author Caitlin Ross
 */
public class SavedImages extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /** List of Image objects */
    ArrayList<Image> imageList;
    /** Adapter to populate the ListView */
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
        NavigationDrawer.init(this, this, toolbar, Activity.SAVED);

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
            viewImage.putExtras(selectedImage.getBundle());
            startActivity(viewImage);
        });

        // Check if the user just deleted an image
        Bundle deleted = getIntent().getBundleExtra(ViewImage.IMAGE_KEY);
        if(deleted != null) {
            // Extract the image data
            Image deletedImage = new Image(deleted);
            // Show the Snackbar to undo the deletion
            Snackbar.make(imageListView, R.string.fragment_delete_msg, Snackbar.LENGTH_LONG)
                    .setAction(R.string.fragment_undo_delete, (c) -> {
                        dao.saveImage(deletedImage, this);
                        updateList();
                    })
                    .show();
        }
    }

    /**
     * When the user comes back to the activity, the list of images is re-loaded from the database.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateList();
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
     * Delegates the navigation logic to the menus.Toolbar class.
     *
     * @param item the menu item selected
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        com.example.nasapicoftheday.menus.Toolbar.navigate(item, this, Activity.SAVED);
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
        NavigationDrawer.navigate(item, this, Activity.SAVED);
        return false;
    }

    /**
     * Reloads the images from the database and updates the ListView.
     */
    private void updateList() {
        // Reload the images from the database
        ImageDao dao = new ImageDao();
        imageList = new ArrayList<>(dao.loadImages(this));

        // Notify the ListView that the data has updated
        adapter.notifyDataSetChanged();
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
            return position;
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
            imageDate.setText(i.getNasaDate().toString());

            // Set the image name (user-defined)
            TextView imageName = convertView.findViewById(R.id.image_list_name);
            imageName.setText(i.getName());

            return convertView;
        }
    }
}
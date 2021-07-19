package com.example.nasapicoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * The SavedImages class contains the functionality for the Saved Images activity
 */
public class SavedImages extends AppCompatActivity {
    ArrayList<Image> imageArrayList;

    /**
     * Creates the Saved Images activity and add the functionality
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        // Initialize the array list of images
        imageArrayList = new ArrayList<>();

        // Check if an Image object was sent in via a Bundle
        Bundle newImageBundle = this.getIntent().getExtras();
        if (newImageBundle != null) {
            imageArrayList.add((Image)newImageBundle.getSerializable(DownloadImage.ImageQuery.IMAGE_BUNDLE_KEY));
        }

        // When database is implemented, load the images from there

        //Populate the ListView
        ListView imageListView = findViewById(R.id.view_image_list);
    }

    private class ImageListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return imageArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return imageArrayList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            Image i = imageArrayList.get(position);

            return null;
        }
    }
}
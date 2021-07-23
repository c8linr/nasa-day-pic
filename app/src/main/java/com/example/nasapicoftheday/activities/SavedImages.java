package com.example.nasapicoftheday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nasapicoftheday.dao.ImageDao;
import com.example.nasapicoftheday.datamodel.Image;
import com.example.nasapicoftheday.R;

import java.util.ArrayList;

/**
 * The SavedImages class contains the functionality for the Saved Images activity
 */
public class SavedImages extends AppCompatActivity {
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

        // Initialize the array list of images
//        imageList = new ArrayList<>();

        // Check if an Image object was sent in via a Bundle
//        Bundle newImageBundle = this.getIntent().getExtras();
//        if (newImageBundle != null) {
//            int imageId = newImageBundle.getInt(Image.ID_KEY);
//            String imageName = newImageBundle.getString(Image.NAME_KEY);
//            String imageTitle = newImageBundle.getString(Image.TITLE_KEY);
//            String imageDownloadDate = newImageBundle.getString(Image.DL_DATE_KEY);
//            String imageNasaDate = newImageBundle.getString(Image.NASA_DATE_KEY);
//            String imageFileName = newImageBundle.getString(Image.FILE_NAME_KEY);
//            try {
//                Image newImage = new Image(imageId, imageName, imageTitle, imageDownloadDate, imageNasaDate, imageFileName);
//                imageList.add(newImage);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

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
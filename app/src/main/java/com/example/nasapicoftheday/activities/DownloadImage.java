package com.example.nasapicoftheday.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasapicoftheday.dao.ImageDao;
import com.example.nasapicoftheday.datamodel.Date;
import com.example.nasapicoftheday.datamodel.Image;
import com.example.nasapicoftheday.R;
import com.example.nasapicoftheday.menus.Activity;
import com.example.nasapicoftheday.menus.NavigationDrawer;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * The DownloadImage class contains the functionality for the Download Image activity.
 *
 * @author Caitlin Ross
 */
public class DownloadImage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Creates the Download Image activity and adds the functionality.
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the navigation drawer
        NavigationDrawer.init(this, this, toolbar, Activity.DOWNLOAD);

        // Get the Bundle with the date info
        Bundle dateBundle = this.getIntent().getBundleExtra(NewImage.DATE_BUNDLE_KEY);
        Date date = new Date(dateBundle);

        // Create a query to download the image for the provided date
        ImageQuery query = new ImageQuery(this);
        query.execute(date);
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
        com.example.nasapicoftheday.menus.Toolbar.navigate(item, this, Activity.DOWNLOAD);
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
        NavigationDrawer.navigate(item, this, Activity.DOWNLOAD);
        return false;
    }

    /**
     * The ImageRequest class handles the background API call to NASA.
     */
    static class ImageQuery extends AsyncTask<Date, Integer, Image> {
        /** The Activity calling the AsyncTask */
        @SuppressLint("StaticFieldLeak")
        private final AppCompatActivity parentActivity;

        /**
         * Constructor, initializes the context.
         *
         * @param context the context of the calling Activity
         */
        public ImageQuery(AppCompatActivity context) {
            this.parentActivity = context;
        }

        /**
         * Calls NASA's API to download the relevant image.
         *
         * @param dates selected date of the image to be downloaded
         * @return the downloaded image
         */
        @Override
        protected Image doInBackground(Date... dates) {
            Image newImage = null;

            // Update the progress
            publishProgress(0);

            // The entire section of code that uses resources is surrounded in a try-catch block
            try {
                JSONObject nasaResponse = getJSONFromURL(dates[0]);

                // Update the progress
                publishProgress(25);

                // Extract the image's download URL and title
                URL imageURL = new URL(nasaResponse.getString("url"));
                String imageTitle = nasaResponse.getString("title");
                String imageFile = imageTitle + ".jpeg";

                // Update the progress
                publishProgress(50);

                // Download the image from the URL if it doesn't exist on disk
                Bitmap bitmap;
                if(!fileExists(imageFile)) {
                    bitmap = downloadImage(imageURL);
                } else {
                    // This feels useless, but I don't know why
                    bitmap = openImage(imageFile);
                }

                // Update the progress
                publishProgress(75);

                // Save the image
                FileOutputStream outputStream = parentActivity.openFileOutput(imageFile, MODE_PRIVATE);
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                }
                outputStream.flush();
                outputStream.close();

                // Make a new Image object
                newImage = new Image(imageTitle, new Date(), dates[0], imageFile);

                // Update the progress
                publishProgress(100);

            } catch (Exception e) {
                e.printStackTrace();
            }
            // Save the image data to the database
            if(newImage != null) {
                ImageDao dao = new ImageDao();
                dao.saveImage(newImage, parentActivity);
            }

            // Return the image
            return newImage;
        }

        /**
         * Updates the progress bar as progress is made on the download.
         *
         * @param values the values (0-100) indicating the progress
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            ProgressBar progressBar = parentActivity.findViewById(R.id.download_progress_bar);
            progressBar.setProgress(values[0]);
        }

        /**
         * Updates the GUI when the background task is finished executing.
         *
         * @param pic the downloaded image
         */
        @Override
        protected void onPostExecute(Image pic) {
            //Use a Toast to inform the user that the download is complete
            Toast.makeText(parentActivity, R.string.download_complete_msg, Toast.LENGTH_SHORT).show();

            // Remove the "Download in progress..." message
            TextView downloadInProgress = parentActivity.findViewById(R.id.download_progress_label);
            downloadInProgress.setVisibility(View.GONE);

            // Remove the progress bar
            ProgressBar progressBar = parentActivity.findViewById(R.id.download_progress_bar);
            progressBar.setVisibility(View.GONE);

            // Update the name field's hint with the image's title
            EditText suggestedName = parentActivity.findViewById(R.id.download_image_name_field);
            suggestedName.setText(pic.getTitle());

            // Display the image
            ImageView imageView = parentActivity.findViewById(R.id.download_image);
            imageView.setImageBitmap(pic.loadImage(parentActivity));

            // Enable the Save button
            Button saveButton = parentActivity.findViewById(R.id.download_save_name_button);
            saveButton.setEnabled(true);
            Intent goToSavedImages = new Intent(parentActivity, SavedImages.class);
            saveButton.setOnClickListener((click) -> {
                ImageDao dao = new ImageDao();
                dao.updateImage(pic, suggestedName.getText().toString(), parentActivity);
                parentActivity.startActivity(goToSavedImages);
            });
        }

        /**
         * Helper method to determine if a file already exists.
         *
         * @param fileName the name of the file to verify
         * @return true if the file already exists
         */
        private boolean fileExists(String fileName) {
            File file = parentActivity.getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }

        /**
         * Uses the given date to generate a JSONObject using the NASA Image of the Day API.
         *
         * @param date the date the user selected
         * @return a JSONObject containing the relevant information about the Image of the Day
         * @throws Exception if something goes wrong
         */
        private JSONObject getJSONFromURL(Date date) throws Exception {
            InputStream response = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            JSONObject json;
            try{
                // Create a URL with the given date to query NASA for the image's download URL
                URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=CD2JkCnbAMdQpZ4O3a0vxBrnRfpIQVJn4fGUp1Sz&date=" + date.toString());
                connection = (HttpURLConnection) url.openConnection();
                response = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);

                // Add all lines from the response into a StringBuilder
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

                json =  new JSONObject(sb.toString());
            } finally {
                // Close the current connection before opening the next
                if (reader != null ) {
                    reader.close();
                }
                if (response != null) {
                    response.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return json;
        }

        /**
         * Downloads the image from the given URL.
         *
         * @param imageURL the URL where the image is located
         * @return a Bitmap of the image
         * @throws Exception if something goes wrong
         */
        private Bitmap downloadImage(URL imageURL) throws Exception {
            Bitmap bitmap = null;
            HttpURLConnection conn = (HttpURLConnection) imageURL.openConnection();
            conn.connect();
            if (conn.getResponseCode() == 200) {
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            }
            return bitmap;
        }

        /**
         * Opens the image from the disk
         *
         * @param fileName the name of the image file
         * @return a Bitmap of the image
         */
        private Bitmap openImage(String fileName) {
            FileInputStream inputStream;
            Bitmap bitmap = null;
            try {
                inputStream = parentActivity.openFileInput(fileName);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
            }
            return bitmap;
        }
    }
}
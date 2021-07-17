package com.example.nasapicoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class DownloadImage extends AppCompatActivity {
    /**
     * Creates the Download Image activity and adds the functionality.
     *
     * @param savedInstanceState a Bundle passed in when the Download Image activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);

        // Get the Bundle with the date info
        Bundle dateBundle = this.getIntent().getBundleExtra(NewImage.PACKAGE_PREFIX + "DateSelected");
        int day = dateBundle.getInt(NewImage.DatePickerFragment.DAY_KEY);
        int month = dateBundle.getInt(NewImage.DatePickerFragment.MONTH_KEY);
        int year = dateBundle.getInt(NewImage.DatePickerFragment.YEAR_KEY);

        // Create a query to download the image for the provided date
        ImageQuery query = new ImageQuery(this);
        query.execute(day, month, year);
    }

    /**
     * The ImageRequest class handles the background API call to NASA
     */
    static class ImageQuery extends AsyncTask<Integer, Integer, Bitmap> {
        private final AppCompatActivity parentActivity;
        private String imageTitle = "New Image";

        public ImageQuery(AppCompatActivity context) {
            this.parentActivity = context;
        }
        /**
         * Calls NASA's API to download the relevant image.
         *
         * @param ints the day, month, and year of the image to be downloaded
         * @return the downloaded image
         */
        @Override
        protected Bitmap doInBackground(Integer... ints) {
            Bitmap image = null;

            // Update the progress
            publishProgress(0);

            try {
                // Connect to the NASA API to get the image's URL and title
                URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=CD2JkCnbAMdQpZ4O3a0vxBrnRfpIQVJn4fGUp1Sz");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream response = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);

                // Add all lines from the response into a StringBuilder
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

                // Update the progress
                publishProgress(25);

                // Convert the response to a JSON Object and extract the image's URL and title
                JSONObject nasaResponse = new JSONObject(sb.toString());
                URL imageURL = new URL(nasaResponse.getString("url"));
                imageTitle = nasaResponse.getString("title");
                String imageFile = imageTitle + ".jpeg";

                // Close the current connection before opening the next
                response.close();
                conn.disconnect();

                // Update the progress
                publishProgress(50);

                // Connect to the image's URL, unless it already exists on disk
                if(fileExists(imageFile)) {
                    FileInputStream inputStream = null;
                    try {
                        inputStream = parentActivity.openFileInput(imageFile);
                    } catch (FileNotFoundException fe) {
                        fe.printStackTrace();
                    }
                    image = BitmapFactory.decodeStream(inputStream);
                } else {
                    conn = (HttpURLConnection) imageURL.openConnection();
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        image = BitmapFactory.decodeStream(conn.getInputStream());
                    }
                }
                // Update the progress
                publishProgress(75);

                // Save the image
                FileOutputStream outputStream = parentActivity.openFileOutput(imageFile, MODE_PRIVATE);
                if (image != null) {
                    image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                }
                outputStream.flush();
                outputStream.close();

                // Update the progress
                publishProgress(100);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        /**
         * Updates the progress bar as progress is made on the download
         *
         * @param values the values (0-100) indicating the progress
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            ProgressBar progressBar = parentActivity.findViewById(R.id.download_progress_bar);
            progressBar.setProgress(values[0]);
        }

        /**
         * Called when the task is finished executing
         *
         * @param pic the downloaded image
         */
        @Override
        protected void onPostExecute(Bitmap pic) {
            // Hide the "Download in progress..." message
            TextView downloadInProgress = parentActivity.findViewById(R.id.download_progress_label);
            downloadInProgress.setVisibility(View.INVISIBLE);

            // Update the name field's hint with the image's title
            EditText suggestedName = parentActivity.findViewById(R.id.download_image_name_field);
            suggestedName.setText(imageTitle);

            // Display the image
            ImageView imageView = parentActivity.findViewById(R.id.download_image);
            imageView.setImageBitmap(pic);
        }

        /**
         * Helper method to determine if a file already exists
         * @param fileName the name of the file to verify
         * @return true if the file already exists
         */
        private boolean fileExists(String fileName) {
            File file = parentActivity.getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }
    }
}
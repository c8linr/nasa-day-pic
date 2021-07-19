package com.example.nasapicoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * The DownloadImage class contains the functionality for the Download Image activity.
 *
 * @author Caitlin Ross
 */
public class DownloadImage extends AppCompatActivity {
    /**
     * Creates the Download Image activity and adds the functionality.
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);

        // Get the Bundle with the date info
        Bundle dateBundle = this.getIntent().getBundleExtra(NewImage.DATE_BUNDLE_KEY);
        int day = dateBundle.getInt(NewImage.DatePickerFragment.DAY_KEY);
        int month = dateBundle.getInt(NewImage.DatePickerFragment.MONTH_KEY);
        int year = dateBundle.getInt(NewImage.DatePickerFragment.YEAR_KEY);

        // Create a query to download the image for the provided date
        ImageQuery query = new ImageQuery(this);
        query.execute(year, month, day);
    }

    /**
     * The ImageRequest class handles the background API call to NASA.
     */
    static class ImageQuery extends AsyncTask<Integer, Integer, Image> {
        /** The Activity calling the AsyncTask */
        private final AppCompatActivity parentActivity;

        public static final String IMAGE_BUNDLE_KEY = "com.example.nasapicoftheday.DownloadedImage";

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
        protected Image doInBackground(Integer... ints) {
            Image newImage = null;
            String dateString = ints[0].toString() + "-" + ints[1].toString() + "-" + ints[2].toString();

            // Update the progress
            publishProgress(0);

            try {
                // Connect to the NASA API to get the image's URL and title
                URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=CD2JkCnbAMdQpZ4O3a0vxBrnRfpIQVJn4fGUp1Sz&date=" + dateString);
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
                String imageTitle = nasaResponse.getString("title");
                String imageFile = imageTitle + ".jpeg";

                // Close the current connection before opening the next
                response.close();
                conn.disconnect();

                // Update the progress
                publishProgress(50);

                // Connect to the image's URL if it doesn't exist on disk
                Bitmap bitmap = null;
                if(!fileExists(imageFile)) {
                    conn = (HttpURLConnection) imageURL.openConnection();
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                    }
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
                newImage = new Image(imageTitle, new Date(), Image.getDateFromInts(ints[0], ints[1], ints[2]), imageFile);

                // Update the progress
                publishProgress(100);

            } catch (Exception e) {
                e.printStackTrace();
            }
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
         * Called when the task is finished executing.
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
            Bundle newImageBundle = new Bundle();
            newImageBundle.putSerializable(IMAGE_BUNDLE_KEY, pic);
            saveButton.setOnClickListener((click) -> parentActivity.startActivity(goToSavedImages, newImageBundle));
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
    }
}
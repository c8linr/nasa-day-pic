package com.example.nasapicoftheday.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.nasapicoftheday.datamodel.Date;
import com.example.nasapicoftheday.R;
import com.example.nasapicoftheday.menus.Activity;
import com.example.nasapicoftheday.menus.NavigationDrawer;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

/**
 * The NewImage class contains the functionality for the New Image Activity.
 *
 * @author Caitlin Ross
 */
public class NewImage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /** Static constant for requesting the Date bundle */
    public static final String DATE_BUNDLE_KEY = "com.example.nasapicoftheday.DateSelected";

    /**
     * Creates the New Image activity and adds the functionality.
     *
     * @param savedInstanceState data supplied if the activity is being re-initialized, otherwise null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the navigation drawer
        NavigationDrawer.init(this, this, toolbar, Activity.NEW);

        // Get a reference to the "Date Selected: " TextView to be modified by the Date Picker
        TextView dateSelected = findViewById(R.id.new_date);

        // Set up the Confirm button
        Button confirmDateButton = findViewById(R.id.new_confirm_button);
        Intent goToDownloadImage = new Intent(this, DownloadImage.class);
        confirmDateButton.setOnClickListener( (click) -> startActivity(goToDownloadImage));
        // Just in case, disable the button. This should be redundant.
        confirmDateButton.setEnabled(false);

        // Create the date picker dialog when the user selects the "select date" button
        Button selectDateButton = findViewById(R.id.new_select_date_button);
        DialogFragment dateFragment = new DatePickerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.setFragmentResultListener(DatePickerFragment.DATE_REQUEST_KEY, this, (requestKey, result) -> {
            int year = result.getInt(Date.YEAR_KEY);
            int month = result.getInt(Date.MONTH_KEY);
            int day = result.getInt(Date.DAY_KEY);
            try{
                Date date = new Date(year, month, day);
                String dateString = getString(R.string.new_selected_date) +
                        " " + Date.getMonthName(month) +
                        " " + day +
                        ", " + year;
                dateSelected.setText(dateString);
                confirmDateButton.setEnabled(true);
                goToDownloadImage.putExtra(DATE_BUNDLE_KEY, date.getBundle());
            } catch (IllegalArgumentException e) {
                // This is reached if the date is invalid
                dateSelected.setText(getString(R.string.new_date_error));
                confirmDateButton.setEnabled(false);
            }
        });
        selectDateButton.setOnClickListener( (click) -> dateFragment.show(fragmentManager, "datePicker"));
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
        com.example.nasapicoftheday.menus.Toolbar.navigate(item, this, Activity.NEW);
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
        NavigationDrawer.navigate(item, this, Activity.NEW);
        return false;
    }

    /**
     * The DatePickerFragment class contains the functionality for the date picker used in the New Image Activity.
     */
     public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        /**
         * Static constant provided to prevent errors when accessing the Bundle with the date.
         */
         public static final String DATE_REQUEST_KEY = "REQUEST_DATE";

         /**
          * Creates the date picker dialog and auto-fills the current date.
          *
          * @param savedInstanceState a Bundle passed in when the dialog is created
          * @return a new date picker dialog object, initialized with the current date
          */
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the current date to use as the default
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // return a new instance of DatePickerDialog, using the current date
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

         /**
          * Creates a Bundle with the date as integers to be accessed by the host.
          *
          * @param view the DatePicker object being used
          * @param year the year selected by the user
          * @param month the month (0-11) selected by the user
          * @param dayOfMonth the day of the month (1-31) selected by the user
          */
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            // Need to adjust month up by one, since the Date Picker uses 0-11
            Date date = new Date(year, (month + 1), dayOfMonth);
            getParentFragmentManager().setFragmentResult(DATE_REQUEST_KEY, date.getBundle());
        }
    }
}
package com.example.nasapicoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * The NewImage class contains the functionality for the New Image Activity.
 *
 * @author Caitlin Ross
 */
public class NewImage extends AppCompatActivity {
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
            int year = result.getInt(CustomDate.YEAR_KEY);
            int month = result.getInt(CustomDate.MONTH_KEY);
            int day = result.getInt(CustomDate.DAY_KEY);
            CustomDate date = new CustomDate(year, month, day);
            dateSelected.append(" " + getMonthName(month) +
                " " + day +
                ", " + year);
            confirmDateButton.setEnabled(true);
            goToDownloadImage.putExtra(DATE_BUNDLE_KEY, date.getBundle());
        });
        selectDateButton.setOnClickListener( (click) -> dateFragment.show(fragmentManager, "datePicker"));
    }

    /**
     * Returns the name of the month, given an integer from 0-11.
     *
     * @param month an integer from 0-11 representing a month
     * @return the full name of the month, capitalized, or a blank string if the argument is invalid
     */
    private String getMonthName(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "";
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
            CustomDate date = new CustomDate(year, month, dayOfMonth);
            getParentFragmentManager().setFragmentResult(DATE_REQUEST_KEY, date.getBundle());
        }
    }
}
package com.example.nasapicoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

    /**
     * Object-level references that are needed both in the onCreate method and DatePickerFragment.onDateSet method
     */
    private static TextView dateSelected;
    private static Button confirmDateButton;

    /**
     * The onCreate method creates the New Image activity and adds the functionality.
     *
     * @param savedInstanceState a Bundle passed in when the activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);

        // Get a reference to the "Date Selected: " TextView to be modified by the Date Picker
        dateSelected = findViewById(R.id.new_date);

        // Set up the Confirm button
        confirmDateButton = findViewById(R.id.new_confirm_button);
        Intent goToDownloadImage = new Intent(this, DownloadImage.class);
        confirmDateButton.setOnClickListener( (click) -> startActivity(goToDownloadImage));
        // Just in case, disable the button. This should be redundant.
        confirmDateButton.setEnabled(false);

        // Create the date picker dialog when the user selects the "select date" button
        Button selectDateButton = findViewById(R.id.new_select_date_button);
        DialogFragment dateFragment = new DatePickerFragment();
        selectDateButton.setOnClickListener( (click) -> dateFragment.show(getSupportFragmentManager(), "datePicker"));
    }

    /**
     * The DatePickerFragment class contains the functionality for the date picker used in the New Image Activity.
     */
     public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

         /**
          * The onCreateDialog method creates the date picker dialog and auto-fills the current date.
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
          * The onDateSet method adds the selected date as a String to the dateSelected Textview and enables the confirmDate button.
          *
          * @param view the DatePicker object being used
          * @param year the year selected by the user
          * @param month the month (0-11) selected by the user
          * @param dayOfMonth the day of the month (1-31) selected by the user
          */
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateSelected.append(" " + getMonthName(month) + " " + dayOfMonth + ", " + year);
            confirmDateButton.setEnabled(true);
        }

        /**
         * getMonthName returns the name of the month, given an integer from 0-11.
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
    }
}
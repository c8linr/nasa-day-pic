package com.example.nasapicoftheday.menus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.nasapicoftheday.R;

/**
 * The CallingActivity enum represents the possible activities that can use the navigation drawer.
 */
public enum Activity {
    MAIN(R.id.main_drawer_layout, R.string.welcome_help_title, R.string.welcome_help_msg),
    NEW(R.id.new_image_drawer_layout, R.string.new_image_help_title, R.string.new_image_help_msg),
    DOWNLOAD(R.id.download_image_drawer_layout, R.string.download_image_help_title, R.string.download_image_help_msg),
    SAVED(R.id.saved_images_drawer_layout, R.string.saved_images_help_title, R.string.saved_images_help_msg);

    private final int drawerLayoutID;
    private final int helpTitleStringID;
    private final int helpMessageStringID;

    /**
     * Constructor, initializes the ID of the relevant DrawerLayout.
     *
     * @param drawerLayoutID the resource ID associated with the DrawerLayout widget
     * @param helpTitleStringID the resource ID associated with the help dialog title
     * @param helpMessageStringID the resource ID associated with the help dialog message
     */
    Activity(int drawerLayoutID, int helpTitleStringID, int helpMessageStringID) {
        this.drawerLayoutID = drawerLayoutID;
        this.helpTitleStringID = helpTitleStringID;
        this.helpMessageStringID = helpMessageStringID;
    }

    /**
     * Returns the DrawerLayout widget.
     *
     * @param parent the activity calling this method
     * @return the DrawerLayout widget associated with this CallingActivity
     */
    public DrawerLayout getLayout(AppCompatActivity parent) {
        return parent.findViewById(drawerLayoutID);
    }

    /**
     * Returns the title of the help dialog.
     *
     * @param parent the activity calling this method
     * @return a String with the title of the help dialog
     */
    public String getHelpTitle(AppCompatActivity parent) {
        return parent.getString(helpTitleStringID);
    }

    /**
     * Returns the message body of the help dialog.
     *
     * @param parent te activity calling this method
     * @return a String with the message to be displayed by the help dialog
     */
    public String getHelpMessage(AppCompatActivity parent) {
        return parent.getString(helpMessageStringID);
    }
}
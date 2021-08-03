package com.example.nasapicoftheday.menus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.nasapicoftheday.R;

/**
 * The Activity enum represents the possible activities that can use the navigation drawer.
 *
 * @author Caitlin Ross
 */
public enum Activity {
    MAIN(R.id.main_drawer_layout,
            R.string.drawer_header_welcome,
            R.string.welcome_help_title,
            R.string.welcome_help_msg),
    NEW(R.id.new_image_drawer_layout,
            R.string.drawer_header_new_image,
            R.string.new_image_help_title,
            R.string.new_image_help_msg),
    DOWNLOAD(R.id.download_image_drawer_layout,
            R.string.drawer_header_download_image,
            R.string.download_image_help_title,
            R.string.download_image_help_msg),
    SAVED(R.id.saved_images_drawer_layout,
            R.string.drawer_header_saved_images,
            R.string.saved_images_help_title,
            R.string.saved_images_help_msg);

    private final int drawerLayoutID;
    private final int drawerActivityNameID;
    private final int helpTitleStringID;
    private final int helpMessageStringID;

    /**
     * Constructor, initializes the IDs.
     *
     * @param drawerLayoutID the resource ID associated with the DrawerLayout widget
     * @param drawerActivityNameID the String ID associated with the name of the current activity
     * @param helpTitleStringID the String ID associated with the help dialog title
     * @param helpMessageStringID the String ID associated with the help dialog message
     */
    Activity(int drawerLayoutID, int drawerActivityNameID, int helpTitleStringID, int helpMessageStringID) {
        this.drawerLayoutID = drawerLayoutID;
        this.drawerActivityNameID = drawerActivityNameID;
        this.helpTitleStringID = helpTitleStringID;
        this.helpMessageStringID = helpMessageStringID;
    }

    /**
     * Returns the DrawerLayout widget.
     *
     * @param parent the activity calling this method
     * @return the DrawerLayout widget associated with this Activity
     */
    public DrawerLayout getLayout(AppCompatActivity parent) {
        return parent.findViewById(drawerLayoutID);
    }

    /**
     * Returns the String containing the name of the activity.
     *
     * @param parent the activity calling this method
     * @return a String containing the name of the activity
     */
    public String getActivityName(AppCompatActivity parent) {
        return parent.getString(drawerActivityNameID);
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
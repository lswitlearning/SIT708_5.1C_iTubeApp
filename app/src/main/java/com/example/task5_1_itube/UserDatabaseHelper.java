package com.example.task5_1_itube;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    // Constants for database name and version
    private static final String DATABASE_NAME = "users.db"; // Name of the database
    private static final int DATABASE_VERSION = 2; // Version of the database

    // Constants for the users table and its columns
    private static final String TABLE_USERS = "users"; // Name of the users table
    private static final String COLUMN_ID = "id"; // Column for user ID (primary key)
    private static final String COLUMN_USERNAME = "username"; // Column for username
    private static final String COLUMN_PASSWORD = "password"; // Column for password

    // Constants for the playlist table and its columns
    private static final String PLAYLIST_TABLE = "playlist"; // Name of the playlist table
    private static final String COLUMN_LINK = "link"; // Column for storing playlist links

    // SQL statement to create the users table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT)"; // Creates a table with ID, unique username, and password

    // SQL statement to create the playlist table
    private static final String CREATE_TABLE_PLAYLIST =
            "CREATE TABLE " + PLAYLIST_TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LINK + " TEXT NOT NULL)"; // Creates a table with ID and link

    // Constructor to create a new database helper
    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Calls the superclass constructor with the database name and version
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create both tables when the database is created
        db.execSQL(CREATE_TABLE_USERS); // Create the users table
        db.execSQL(CREATE_TABLE_PLAYLIST); // Create the playlist table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // If upgrading from a version older than 2
            // Only add the playlist table, users table remains unaffected
            db.execSQL(CREATE_TABLE_PLAYLIST);
        }
    }

    // Method to add a new user to the database
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database instance
        ContentValues values = new ContentValues(); // Content values to insert into the table
        values.put(COLUMN_USERNAME, username); // Put the username
        values.put(COLUMN_PASSWORD, password); // Put the password

        long result = db.insert(TABLE_USERS, null, values); // Insert the data into the users table
        return result != -1; // If the insertion was successful, return true, otherwise false
    }

    // Method to validate a user with a given username and password
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database instance
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password}); // Execute the query with provided username and password

        boolean isValid = cursor.moveToFirst(); // If a record is found, validation is successful
        cursor.close(); // Close the cursor to avoid memory leaks
        return isValid; // Return the validation result
    }

    // Method to add a link to the playlist
    public boolean addLinkToPlaylist(String link) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database instance
        ContentValues values = new ContentValues(); // Content values to insert into the table
        values.put(COLUMN_LINK, link); // Put the playlist link

        long result = db.insert(PLAYLIST_TABLE, null, values); // Insert the link into the playlist table
        db.close(); // Close the database to release resources
        return result != -1; // If the insertion was successful, return true, otherwise false
    }

    // Method to get all playlist links
    public Cursor getPlaylist() {
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database instance
        return db.rawQuery("SELECT * FROM " + PLAYLIST_TABLE, null); // Return all records from the playlist table
    }
}

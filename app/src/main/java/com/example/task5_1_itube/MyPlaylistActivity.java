package com.example.task5_1_itube;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylistActivity extends AppCompatActivity {
    private RecyclerView recyclerView; // RecyclerView to display the playlist
    private UserDatabaseHelper dbHelper; // Database helper for user data
    private List<String> playlist; // List to store playlist items


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplaylist); // Set the content view to the playlist layout

        // Initialize the database helper and RecyclerView
        dbHelper = new UserDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);

        setupRecyclerView(); // Call method to set up the RecyclerView
    }

    private void setupRecyclerView() {
        playlist = new ArrayList<>(); // Initialize an empty list for the playlist
        Cursor cursor = dbHelper.getPlaylist(); // Get playlist data from the database

        // Check if the cursor is valid
        if (cursor != null) {
            int linkColumnIndex = cursor.getColumnIndex("link"); // Get the index of the "link" column
            if (linkColumnIndex >= 0) {
                // Loop through the cursor to extract the playlist links
                while (cursor.moveToNext()) {
                    String link = cursor.getString(linkColumnIndex); // Get the link from the cursor
                    playlist.add(link); // Add the link to the playlist
                }
                cursor.close(); // Close the cursor after use
            } else {
                // Show a toast message if the "link" column is not found
                Toast.makeText(this, "Column 'link' not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show a toast message if the cursor is null
            Toast.makeText(this, "Failed to retrieve playlist", Toast.LENGTH_SHORT).show();
        }

        // Create an adapter with the playlist data
        PlaylistAdapter adapter = new PlaylistAdapter(playlist);

        // Set the layout manager to LinearLayoutManager for vertical scrolling
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Attach the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    // RecyclerView adapter for displaying the playlist
    private static class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
        private final List<String> playlist; // List of playlist links

        PlaylistAdapter(List<String> playlist) {
            this.playlist = playlist; // Store the playlist data in the adapter
        }

        @Override
        public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the playlist item layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
            return new PlaylistViewHolder(view); // Create and return a new ViewHolder
        }

        @Override
        public void onBindViewHolder(PlaylistViewHolder holder, int position) {
            // Set the text of the TextView to the playlist link
            String link = playlist.get(position);
            holder.linkTextView.setText(link);
        }

        @Override
        public int getItemCount() {
            return playlist.size(); // Return the number of items in the playlist
        }

        // ViewHolder class for individual playlist items
        static class PlaylistViewHolder extends RecyclerView.ViewHolder {
            TextView linkTextView; // TextView to display the playlist link

            PlaylistViewHolder(View itemView) {
                super(itemView);
                linkTextView = itemView.findViewById(R.id.linkTextView); // Initialize the TextView
            }
        }
    }
}

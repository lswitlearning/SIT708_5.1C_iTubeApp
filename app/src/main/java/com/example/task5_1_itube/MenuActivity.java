package com.example.task5_1_itube;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuActivity extends AppCompatActivity {
    private EditText youtubeLinkEditText; // Input field for YouTube link
    private Button playButton; // Button to play video
    private Button addToPlaylistButton; // Button to add video to playlist
    private Button myPlaylistButton; // Button to view playlist
    private UserDatabaseHelper dbHelper; // Database instance to interact with user data
    private final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"; // Regular expression for matching YouTube URLs


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); // Set the content view to the menu layout

        // Initialize UI elements
        youtubeLinkEditText = findViewById(R.id.linkTextView);
        playButton = findViewById(R.id.playButton);
        addToPlaylistButton = findViewById(R.id.addToPlaylistButton);
        myPlaylistButton = findViewById(R.id.myPlaylistButton);

        // Create the database helper instance
        dbHelper = new UserDatabaseHelper(this);

        // Set "Add to Playlist" button click event
        addToPlaylistButton.setOnClickListener(v -> {
            String youtubeLink = youtubeLinkEditText.getText().toString().trim(); // Get the YouTube link from the input field

            if (!youtubeLink.isEmpty()) {
                // Add the YouTube link to the database playlist
                boolean isAdded = dbHelper.addLinkToPlaylist(youtubeLink);

                if (isAdded) {
                    Toast.makeText(this, "Added to Playlist", Toast.LENGTH_SHORT).show(); // Notify user of success
                } else {
                    Toast.makeText(this, "Failed to add to Playlist", Toast.LENGTH_SHORT).show(); // Notify user of failure
                }
            } else {
                Toast.makeText(this, "Please enter a YouTube link", Toast.LENGTH_SHORT).show(); // Prompt user to enter a link
            }
        });

        // Set "Play" button click event
        playButton.setOnClickListener(v -> {
            String youtubeLink = youtubeLinkEditText.getText().toString().trim(); // Get the YouTube link from the input field

            if (!youtubeLink.isEmpty()) {
                // Extract the YouTube video ID from the link
                String videoId = extractVideoIdFromUrl(youtubeLink);

                if (videoId != null && !videoId.isEmpty()) {
                    // Start the VideoPlayActivity to play the YouTube video
                    Intent intent = new Intent(MenuActivity.this, VideoPlayActivity.class);
                    intent.putExtra("VIDEO_ID", videoId); // Pass the video ID to the new activity
                    startActivity(intent); // Start the new activity
                } else {
                    Toast.makeText(MenuActivity.this, "Invalid YouTube Link", Toast.LENGTH_SHORT).show(); // Notify user if the link is invalid
                }
            } else {
                Toast.makeText(MenuActivity.this, "Please enter a YouTube link", Toast.LENGTH_SHORT).show(); // Prompt user to enter a link
            }
        });

        // Set "My Playlist" button click event
        myPlaylistButton.setOnClickListener(v -> {
            // Start MyPlaylistActivity to view the playlist
            Intent intent = new Intent(MenuActivity.this, MyPlaylistActivity.class);
            startActivity(intent); // Start the new activity
        });
    }

    private String extractVideoIdFromUrl(String url) {
        // Remove protocol and domain from the YouTube link
        String cleanedUrl = youTubeLinkWithoutProtocolAndDomain(url);

        // Define possible patterns for extracting video ID
        final String[] videoIdPatterns = {
                "\\?vi?=([^&]*)",
                "watch\\?.*v=([^&]*)",
                "(?:embed|vi?)/([^/?]*)",
                "^([A-Za-z0-9\\-]*)"
        };

        // Try to find a match for video ID in the cleaned URL
        for (String pattern : videoIdPatterns) {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(cleanedUrl);

            if (matcher.find()) {
                return matcher.group(1); // Return the matched video ID
            }
        }

        return null; // Return null if no video ID is found
    }

    private String youTubeLinkWithoutProtocolAndDomain(String url) {
        // Remove the protocol and domain from the given YouTube URL
        Pattern pattern = Pattern.compile(youTubeUrlRegEx);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return url.replace(matcher.group(), ""); // Remove the protocol and domain part
        }

        return url; // Return the original URL if no match is found
    }
}

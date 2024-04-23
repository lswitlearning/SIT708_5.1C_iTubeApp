package com.example.task5_1_itube;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplayer); // Set the content view to the videoplayer layout

        YouTubePlayerView youTubePlayerView = findViewById(R.id.videoPlayer); // Find the YouTube player view in the layout
        String videoId = getIntent().getStringExtra("VIDEO_ID"); // Get the video ID from the Intent extras

        if (videoId == null || videoId.isEmpty()) {
            // If the video ID is invalid or not provided, show a Toast message and return
            Toast.makeText(this, "Invalid video ID", Toast.LENGTH_SHORT).show();
            return; // Exit the method to avoid further execution
        }

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                // When the YouTube player is ready, load the video with the specified ID from the beginning
                youTubePlayer.loadVideo(videoId, 0); // Load the video and start playing from the beginning
            }
        });
    }
}

/*
 * Copyright 2015 Shoaib Khan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.shoaib.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SearchArtistActivity extends AppCompatActivity
        implements SearchArtistFragment.ArtistListCallback,
        TopTracksActivityFragment.TrackListCallback{

    /**
     * Gives the user ability to search for an artist, view the list
     * of artists and choose one for details
     */

    /**
     * TODO: implement Tablet layout
     * TODO: implement Music Player features - play, pause, next, previous
     * TODO: implement sharing
     * TODO: implement Settings menu
     * TODO: implement Notification
     *
     * TODO: layout when no artist is found
     * TODO: implement sqlist db, content provider for previously searched artists
     * TODO: use progressive search
     * TODO: implement Playback dialog
     * TODO: create app icon
     * TODO: use Material Design theme
     */

    public static final String ARTIST_ID = "artist_id";
    boolean mTwoPane;

    private static final String TAG = SearchArtistActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artist);

        if(findViewById(R.id.tracks_container) != null) {
            // we are in Tablet layout
            mTwoPane = true;
            ((SearchArtistFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_artists))
                    .setActivateOnItemClick(true);
            Log.d(TAG, "Tablet Layout");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_artist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArtistSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the tracks view in this activity by
            // adding or replacing the tracks fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ARTIST_ID, id);
            //Log.d(TAG, "ArtistId: " + id);
            TopTracksActivityFragment fragment = new TopTracksActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tracks_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected artist id.
            Intent intent = new Intent(this, TopTracksActivity.class);
            intent.putExtra(ARTIST_ID, id);
            startActivity(intent);
        }
    }

    @Override
    public void onTrackSelected(TrackData track) {
        Log.d(TAG, track.toString());
        showMusicPlayerDialog();
    }

    public void showMusicPlayerDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PlaybackDialogFragment newFragment = new PlaybackDialogFragment();

        if (mTwoPane) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.replace(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }
    }
}

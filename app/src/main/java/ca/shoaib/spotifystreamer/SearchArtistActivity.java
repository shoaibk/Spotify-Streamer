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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchArtistActivity extends AppCompatActivity {

    /**
     * Gives the user ability to search for an artist, view the list
     * of artists and choose one for details
     */

    /**
     * TODO: use fragment for SearchArtistActivity
     * TODO: create and show layout when no artist is found
     * TODO: use progressive search
     * TODO: create Playback activity
     */

    public static final String ARTIST_ID = "artist_id";

    private static final String TAG = SearchArtistActivity.class.getSimpleName();
    public static final String KEY_ARTISTS = "artists";
    private ArrayList<ArtistData> artistList;
    private ArtistAdapter artistAdapter;
    private EditText searchInput;
    private Toast noInternetToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artist);

        searchInput = (EditText) findViewById(R.id.search_artist);
        artistList = new ArrayList<>();

        ListView artistListView = (ListView)findViewById(R.id.list_artist);
        if( savedInstanceState != null ) {
            this.artistList = savedInstanceState.getParcelableArrayList(KEY_ARTISTS);
        }
        artistAdapter = new ArtistAdapter(this,
                android.R.layout.simple_list_item_1,
                this.artistList);
        artistListView.setAdapter(artistAdapter);

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String searchText = searchInput.getText().toString();

                    if(!searchText.isEmpty()) {
                        if(Utilities.isOnline(getApplicationContext())) {
                            SearchArtistTask searchArtistTask = new SearchArtistTask(getApplicationContext(), SearchArtistActivity.this.artistList, artistAdapter);
                            searchArtistTask.execute(searchText);
                        } else {
                            noInternetToast = Utilities.showToast(noInternetToast, getApplicationContext(), getString(R.string.no_internet_toast));
                        }

                        // hide keyboard
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(v.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        handled = true;
                    }
                }
                return handled;
            }
        });


        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Utilities.isOnline(getApplicationContext())) {
                    Intent intent = new Intent(getApplicationContext(), TopTracksActivity.class);
                    ArtistData chosenArtist = SearchArtistActivity.this.artistList.get(position);
                    intent.putExtra(ARTIST_ID, chosenArtist.getArtistId());
                    startActivity(intent);
                } else {
                    noInternetToast = Utilities.showToast(noInternetToast, getApplicationContext(), getString(R.string.no_internet_toast));
                }
            }
        });
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_ARTISTS, artistList);
    }

}

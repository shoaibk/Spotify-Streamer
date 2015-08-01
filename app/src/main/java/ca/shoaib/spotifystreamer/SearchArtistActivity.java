package ca.shoaib.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

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
    private ArrayList<ArtistData> artistsInListView;
    private ArtistAdapter adapter;
    private EditText searchInput;
    private Toast noInternetToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artist);

        searchInput = (EditText) findViewById(R.id.search_artist);
        artistsInListView = new ArrayList<>();

        ListView artistList = (ListView)findViewById(R.id.list_artist);
        if( savedInstanceState != null ) {
            artistsInListView = savedInstanceState.getParcelableArrayList(KEY_ARTISTS);
        }
        adapter = new ArtistAdapter(this,
                android.R.layout.simple_list_item_1,
                artistsInListView);
        artistList.setAdapter(adapter);

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String searchText = searchInput.getText().toString();

                    if(!searchText.isEmpty()) {
                        if(Utilities.isOnline(getApplicationContext())) {
                            new SearchArtistTask().execute(searchText);
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


        artistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Utilities.isOnline(getApplicationContext())) {
                    Intent intent = new Intent(getApplicationContext(), TopTracksActivity.class);
                    ArtistData chosenArtist = artistsInListView.get(position);
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
        outState.putParcelableArrayList(KEY_ARTISTS, artistsInListView);
    }


    private class SearchArtistTask extends AsyncTask<String, Integer, List<ArtistData>> {

        protected List<ArtistData> doInBackground(String... artistName) {

            List<ArtistData> artistListData = new ArrayList<>();

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                ArtistsPager results = spotify.searchArtists(artistName[0]);

                for(Artist artist: results.artists.items) {
                    ArtistData artistData = new ArtistData(artist);
                    artistListData.add(artistData);
                    Log.d(TAG, artistData.toString());
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception", e);
            }

            return artistListData;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(List<ArtistData> artists) {
            if(artists.isEmpty()){
                Toast.makeText(getApplicationContext(), R.string.no_artist_toast, Toast.LENGTH_LONG).show();

            } else {
                artistsInListView.clear();
                artistsInListView.addAll(artists);
                adapter.notifyDataSetChanged();
            }

        }
    }
}

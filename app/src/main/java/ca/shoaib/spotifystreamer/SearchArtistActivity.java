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
     * TODO: When TopTracksActicity returns, it should not create new activity again, but just show
     * the previous search result
     * TODO: create TopTracksActivity, create layouts, create adapter
     * TODO: create Playback activity
     * TODO: create db and use
     * TODO: use progressive search
     */

    public static final String DEBUG_TAG = SearchArtistActivity.class.getSimpleName();
    private List<Artist> artistsInListView;
    private ArtistAdapter adapter;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artist);

        Log.d(DEBUG_TAG, "In onCreate() ");

        artistsInListView = new ArrayList<>();

        adapter = new ArtistAdapter(this,
                android.R.layout.simple_list_item_1, artistsInListView);

        ListView artistList = (ListView)findViewById(R.id.list_artist);
        artistList.setAdapter(adapter);

        searchInput = (EditText) findViewById(R.id.search_artist);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String searchText = searchInput.getText().toString();

                    if(!searchText.isEmpty()) {
                        SearchArtistTask searchArtistTask = new SearchArtistTask();
                        searchArtistTask.execute(searchText);

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
                Intent intent = new Intent(getApplicationContext(), TopTracksActivity.class);
                //intent.putExtra(MESSAGE_TYPE, m.getMessage_type());

                //intent.putExtra(NEW_MESSAGE, m);
                Artist chosenArtist = (Artist)artistsInListView.get(position);
                intent.putExtra("ArtistId", chosenArtist.id);
                startActivity(intent);
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


    private class SearchArtistTask extends AsyncTask<String, Integer, List<Artist>> {

        protected List<Artist> doInBackground(String... artistName) {

            Log.d(DEBUG_TAG, "In doInBackground: ");
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(artistName[0]);

            return results.artists.items;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(List<Artist> artists) {
            if(artists.isEmpty()){
                Toast.makeText(getApplicationContext(), "No artists", Toast.LENGTH_SHORT).show();

            } else {
                artistsInListView.clear();
                artistsInListView.addAll(artists);
                adapter.notifyDataSetChanged();
            }

        }
    }
}

package ca.shoaib.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SearchArtistActivity extends AppCompatActivity {

    /**
     *
     */

    public static final String DEBUG_TAG = SearchArtistActivity.class.getSimpleName();
    private List<Artist> artistsInListView;
    private ArtistAdapter adapter;

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

        SearchArtistTask searchArtistTask = new SearchArtistTask();
        searchArtistTask.execute("Coldplay");
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
            artistsInListView.clear();
            artistsInListView.addAll(artists);
            adapter.notifyDataSetChanged();
        }
    }
}

package ca.shoaib.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksActivityFragment extends Fragment {

    public static final String TAG = TopTracksTask.class.getSimpleName();

    private TopTracksAdapter adapter;
    private List<Track> tracksInListView;

    public TopTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tracksInListView = new ArrayList<>();
        adapter = new TopTracksAdapter(getActivity(), R.layout.track_row, tracksInListView);

        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_top_tracks);
        listView.setAdapter(adapter);

        Bundle extras = getActivity().getIntent().getExtras();

        String artistId = "";
        if (extras != null) {
            artistId = extras.getString("ArtistId");
        }

        new TopTracksTask().execute(artistId);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {

            }
        });

        return rootView;

    }

    private class TopTracksTask extends AsyncTask<String, Integer, List<Track>> {

        protected List<Track> doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Tracks tracks;

            Map<String, Object> queryMap = new HashMap<>();

            queryMap.put("country", "US");
            tracks = spotify.getArtistTopTrack(params[0], queryMap);

            Log.d(TAG, "In TopTracksTask");
            Log.d(TAG, tracks.toString());
            return tracks.tracks;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(List<Track> tracks) {
            if(tracks.isEmpty()){
                Toast.makeText(getActivity(), "No tracks", Toast.LENGTH_SHORT).show();

            } else {
                tracksInListView.clear();
                tracksInListView.addAll(tracks);
                adapter.notifyDataSetChanged();
            }

        }
    }
}

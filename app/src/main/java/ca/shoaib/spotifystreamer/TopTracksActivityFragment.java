package ca.shoaib.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * A fragment containing List of Tracks for an Artist.
 */
public class TopTracksActivityFragment extends Fragment {

    public static final String TAG = TopTracksActivityFragment.class.getSimpleName();
    private static final String KEY_TRACKS = "tracks";

    private TopTracksAdapter adapter;
    private ArrayList<TrackData> tracksInListView;

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

        if( savedInstanceState != null ) {
            tracksInListView = savedInstanceState.getParcelableArrayList(KEY_TRACKS);
            adapter = new TopTracksAdapter(getActivity(), R.layout.track_row, tracksInListView);
            listView.setAdapter(adapter);
        } else {
            Bundle extras = getActivity().getIntent().getExtras();
            String artistId = "";
            if (extras != null) {
                artistId = extras.getString(SearchArtistActivity.ARTIST_ID);
            }

            if(Utilities.isOnline(getActivity().getApplicationContext())) {
                new TopTracksTask().execute(artistId);
            } else {
                Utilities.showToastOffline(getActivity().getApplicationContext());
            }

        }

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_TRACKS, tracksInListView);
    }

    private class TopTracksTask extends AsyncTask<String, Void, List<TrackData>> {

        protected List<TrackData> doInBackground(String... params) {

            List<TrackData> trackDataList = new ArrayList<>();
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                Tracks tracks;

                Map<String, Object> queryParams = new HashMap<>();

                queryParams.put("country", "US");
                tracks = spotify.getArtistTopTrack(params[0], queryParams);

                for(Track track: tracks.tracks) {
                    TrackData trackData = new TrackData(track);
                    trackDataList.add(trackData);
                    Log.d(TAG, trackData.toString());
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception", e);
            }

            return trackDataList;
        }

        protected void onPostExecute(List<TrackData> tracks) {
            if(tracks.isEmpty()){
                Toast.makeText(getActivity(), R.string.no_track_toast, Toast.LENGTH_LONG).show();
            } else {
                tracksInListView.clear();
                tracksInListView.addAll(tracks);
                adapter.notifyDataSetChanged();
            }

        }
    }
}

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

import android.app.Activity;
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
 * A fragment containing List of Tracks for an Artist.
 */
public class TopTracksActivityFragment extends Fragment {

    public static final String TAG = TopTracksActivityFragment.class.getSimpleName();
    private static final String KEY_TRACKS = "tracks";

    private TopTracksAdapter adapter;
    private ArrayList<TrackData> tracksInListView;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private TrackListCallback mTrackListCallback;

    public TopTracksActivityFragment() {
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface TrackListCallback {
        /**
         * Callback for when an item has been selected.
         */
        public void onTrackSelected(TrackData track);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tracksInListView = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_top_tracks);

        if( savedInstanceState != null ) {
            tracksInListView = savedInstanceState.getParcelableArrayList(KEY_TRACKS);
        } else {
            Bundle extras = getActivity().getIntent().getExtras();
            String artistId = "";
            if (extras != null) {
                artistId = extras.getString(SearchArtistActivity.ARTIST_ID);
            }

            if(Utilities.isOnline(getActivity().getApplicationContext())) {
                new TopTracksTask().execute(artistId);
            }
        }

        adapter = new TopTracksAdapter(getActivity(), R.layout.track_row, tracksInListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTrackListCallback.onTrackSelected(tracksInListView.get(position));
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof TrackListCallback)) {
            throw new IllegalStateException("Activity must implement TrackListCallback.");
        }

        mTrackListCallback = (TrackListCallback) activity;
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
                /*
                use web api to fetch top tracks of the artist. Example url:
                https://api.spotify.com/v1/artists/43ZHCT0cAZBISjO8DG9PnE/top-tracks?country=US
                 */
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

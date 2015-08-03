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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A fragment containing List of Tracks for an Artist.
 */
public class TopTracksActivityFragment extends Fragment {

    public static final String TAG = TopTracksActivityFragment.class.getSimpleName();
    public static final String KEY_TRACKS = "tracks";
    private TopTracksAdapter tracksAdapter;
    private ArrayList<TrackData> trackList;

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
        void onTrackSelected(TrackData track);
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(SearchArtistActivity.ARTIST_ID)) {

            String artistId = getArguments().getString(SearchArtistActivity.ARTIST_ID);
            if(Utilities.isOnline(getActivity().getApplicationContext())) {
                TopTracksTask topTracksTask = new TopTracksTask(getActivity()
                        .getApplicationContext(), trackList, tracksAdapter);
                topTracksTask.execute(artistId);
            }
        }
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        trackList = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_top_tracks);

        tracksAdapter = new TopTracksAdapter(getActivity(), R.layout.track_row, trackList);
        listView.setAdapter(tracksAdapter);

        if( savedInstanceState != null ) {
            trackList = savedInstanceState.getParcelableArrayList(KEY_TRACKS);
        } else {
            if (getArguments().containsKey(SearchArtistActivity.ARTIST_ID)) {

                String artistId = getArguments().getString(SearchArtistActivity.ARTIST_ID);
                if(Utilities.isOnline(getActivity().getApplicationContext())) {
                    TopTracksTask topTracksTask = new TopTracksTask(getActivity()
                            .getApplicationContext(), trackList, tracksAdapter);
                    topTracksTask.execute(artistId);
                }
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTrackListCallback.onTrackSelected(trackList.get(position));
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
        outState.putParcelableArrayList(KEY_TRACKS, trackList);
    }
}

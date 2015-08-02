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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class TopTracksTask extends AsyncTask<String, Void, List<TrackData>> {

    private static final String TAG = TopTracksTask.class.getSimpleName();
    private Context mContext;
    private List<TrackData> mTrackList;
    private TopTracksAdapter mTracksAdapter;

    public TopTracksTask(Context context, List<TrackData> trackList, TopTracksAdapter tracksAdapter) {
        mContext = context;
        mTrackList = trackList;
        mTracksAdapter = tracksAdapter;
    }

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
            Toast.makeText(mContext, R.string.no_track_toast, Toast.LENGTH_LONG).show();
        } else {
            mTrackList.clear();
            mTrackList.addAll(tracks);
            mTracksAdapter.notifyDataSetChanged();
        }

    }
}

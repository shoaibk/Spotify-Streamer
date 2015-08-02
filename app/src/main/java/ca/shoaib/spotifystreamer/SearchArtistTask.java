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
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SearchArtistTask extends AsyncTask<String, Void, List<ArtistData>> {
    private static final String TAG = SearchArtistTask.class.getSimpleName();
    private final Context mContext;
    private List<ArtistData> mArtistList;
    private ArtistAdapter mArtistAdapter;

    public SearchArtistTask(Context context, List<ArtistData> artistList, ArtistAdapter artistAdapter) {
        mContext = context;
        mArtistList = artistList;
        mArtistAdapter = artistAdapter;
    }

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
            // TODO: catch more specific exception
            Log.d(TAG, "Exception", e);
        }

        return artistListData;
    }

    protected void onPostExecute(List<ArtistData> artists) {
        if(artists.isEmpty()){
            Toast.makeText(mContext, R.string.no_artist_toast, Toast.LENGTH_LONG).show();

        } else {
            mArtistList.clear();
            mArtistList.addAll(artists);
            mArtistAdapter.notifyDataSetChanged();
        }

    }
}
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
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A fragment containing the search results of an Artist
 * Activity containing this fragment must implement the
 * Callback interface
 */
public class SearchArtistFragment extends Fragment {
    public static final String TAG = SearchArtistFragment.class.getSimpleName();
    public static final String KEY_ARTISTS = "artists";
    private ArrayList<ArtistData> artistList;
    private ArtistAdapter artistAdapter;
    private ListView mListView;
    private EditText searchInput;
    private Toast noInternetToast;
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks
     */
    private ArtistListCallback mArtistListCallback = sArtistListCallback;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item selections.
     */
    public interface ArtistListCallback {
        /**
         * Callback for when an Artist has been selected
         */
        void onArtistSelected(String id);
    }

    /**
     * A dummy implementation of the callback interface that does nothing.
     * Used only when this fragment is not attached to an activity.
     */
    private static ArtistListCallback sArtistListCallback = new ArtistListCallback() {
        @Override
        public void onArtistSelected(String id) {}
    };

    /**
     * Mandatory empty constructor
     */
    public SearchArtistFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //artistList = new ArrayList<>();
        if( savedInstanceState != null ) {
            artistList = savedInstanceState.getParcelableArrayList(KEY_ARTISTS);
        } else {
            artistList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_artist, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_artist);

        artistAdapter = new ArtistAdapter(getActivity(), R.layout.artist_row, artistList);
        mListView.setAdapter(artistAdapter);
        //mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mArtistListCallback.onArtistSelected(artistList.get(position).getArtistId());
                mActivatedPosition = position;
                setActivatedPosition(position);
                //Log.d(TAG, "ArtistId: " + artistList.get(position).getArtistId());
            }
        });
        searchInput = (EditText) rootView.findViewById(R.id.search_artist);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String searchText = searchInput.getText().toString();

                    if (!searchText.isEmpty()) {
                        if (Utilities.isOnline(getActivity())) {
                            SearchArtistTask searchArtistTask =
                                    new SearchArtistTask(getActivity(), artistList, artistAdapter);
                            searchArtistTask.execute(searchText);
                        } else {
                            noInternetToast = Utilities.showToast(noInternetToast,
                                    getActivity(),
                                    getString(R.string.no_internet_toast));
                        }

                        // hide keyboard
                        InputMethodManager in = (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(v.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        handled = true;
                    }
                }
                return handled;
            }
        });
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof ArtistListCallback)) {
            throw new IllegalStateException("Activity must implement ArtistListCallback callbacks.");
        }

        mArtistListCallback = (ArtistListCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mArtistListCallback = sArtistListCallback;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        outState.putParcelableArrayList(KEY_ARTISTS, artistList);
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mListView.setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            mListView.setItemChecked(mActivatedPosition, false);
        } else {
            mListView.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }


}

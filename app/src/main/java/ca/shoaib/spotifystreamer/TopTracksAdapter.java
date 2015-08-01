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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TopTracksAdapter extends ArrayAdapter<TrackData> {

    Context context;


    public TopTracksAdapter(Context context, int resourceId,
                         List<TrackData> items) {
        super(context, resourceId, items);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        TrackData track = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.track_row, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.track_row_icon);

        String url = track.getImageThumbnailUrl();
        Picasso.with(context).load(url).into(icon);

        TextView trackName = (TextView) convertView.findViewById(R.id.track_name);
        trackName.setText(track.getTrackName());

        TextView albumName = (TextView) convertView.findViewById(R.id.album_name);
        albumName.setText(track.getAlbumName());

        return convertView;
    }

}

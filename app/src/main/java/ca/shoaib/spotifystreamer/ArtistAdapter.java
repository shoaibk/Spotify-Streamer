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

public class ArtistAdapter extends ArrayAdapter<ArtistData> {

    Context context;


    public ArtistAdapter(Context context, int resourceId,
                          List<ArtistData> items) {
        super(context, resourceId, items);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ArtistData artist = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.artist_row, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.artist_row_icon);
        String url = artist.getImageThumbnailUrl();
        Picasso.with(context).load(url).into(icon);

        TextView artistName = (TextView) convertView.findViewById(R.id.artist_row_name);
        artistName.setText(artist.getArtistName());

        return convertView;
    }

}

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

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by khan on 2015-06-20.
 */
public class TopTracksAdapter extends ArrayAdapter<Track> {

    Context context;


    public TopTracksAdapter(Context context, int resourceId,
                         List<Track> items) {
        super(context, resourceId, items);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Track track = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.track_row, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.track_row_icon);
        if(track.album.images.size() > 1) {
            String url = track.album.images.get(1).url;
            Picasso.with(context).load(url).into(icon);
        }

        TextView trackName = (TextView) convertView.findViewById(R.id.track_name);
        trackName.setText(track.name);

        TextView albumName = (TextView) convertView.findViewById(R.id.album_name);
        albumName.setText(track.album.name);

        return convertView;
    }

}

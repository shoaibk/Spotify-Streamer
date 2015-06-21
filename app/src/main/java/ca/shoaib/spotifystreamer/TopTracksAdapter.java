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

/**
 * Created by khan on 2015-06-20.
 */
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

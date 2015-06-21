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

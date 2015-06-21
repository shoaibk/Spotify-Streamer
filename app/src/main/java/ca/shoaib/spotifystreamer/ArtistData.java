package ca.shoaib.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by khan on 2015-06-21.
 */
public class ArtistData implements Parcelable {

    private String artistName;
    private String imageThumbnailUrl;
    private String artistId;

    private static final int IMAGE_THUMBNAIL_WIDTH_MIN = 175;
    private static final int IMAGE_THUMBNAIL_WIDTH_MAX = 350;

    public ArtistData(Artist artist) {
        artistName = artist.name;
        List<Image> images = artist.images;
        for (Image image:images) {
            if(image.width >= IMAGE_THUMBNAIL_WIDTH_MIN
                    && image.width <= IMAGE_THUMBNAIL_WIDTH_MAX) imageThumbnailUrl = image.url;
        }
        artistId = artist.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(imageThumbnailUrl);
        dest.writeString(artistId);
    }

    public static final Parcelable.Creator<ArtistData> CREATOR = new Parcelable.Creator<ArtistData>() {
        public ArtistData createFromParcel(Parcel parcel) {
            return new ArtistData(parcel);
        }

        public ArtistData[] newArray(int size) {
            return new ArtistData[size];
        }
    };

    private ArtistData(Parcel in) {
        artistName = in.readString();
        imageThumbnailUrl = in.readString();
        artistId = in.readString();
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    public String getArtistId() {
        return artistId;
    }

    @Override
    public String toString() {
        return "[Artist] name: " + artistName;
    }
}
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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by khan on 2015-06-20.
 */
public class TrackData implements Parcelable {

    private String trackName;
    private String artistName;
    private String albumName;
    private String imageThumbnailUrl;

    private static final int IMAGE_THUMBNAIL_WIDTH_MIN = 175;
    private static final int IMAGE_THUMBNAIL_WIDTH_MAX = 350;

    public TrackData(Track track) {
        trackName = track.name;
        artistName = track.artists.get(0).name;
        albumName = track.album.name;
        List<Image> images = track.album.images;
        for (Image image:images) {
            if(image.width >= IMAGE_THUMBNAIL_WIDTH_MIN
                    && image.width <= IMAGE_THUMBNAIL_WIDTH_MAX) imageThumbnailUrl = image.url;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trackName);
        dest.writeString(artistName);
        dest.writeString(albumName);
        dest.writeString(imageThumbnailUrl);
    }

    public static final Parcelable.Creator<TrackData> CREATOR = new Parcelable.Creator<TrackData>() {
        public TrackData createFromParcel(Parcel parcel) {
            return new TrackData(parcel);
        }

        public TrackData[] newArray(int size) {
            return new TrackData[size];
        }
    };

    private TrackData(Parcel in) {
        trackName = in.readString();
        artistName = in.readString();
        albumName = in.readString();
        imageThumbnailUrl = in.readString();
    }

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    @Override
    public String toString() {
        return "[Track] name: " + trackName +
                " artist: " + artistName +
                " album: " + albumName;
    }
}

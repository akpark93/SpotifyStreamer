package com.example.andrewpark.spotifystreamer;

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
 * Created by andrewpark on 7/12/15.
 */
public class TrackAdapter extends ArrayAdapter<Track> {

    Picasso mPicasso;

    public TrackAdapter(Context context, int resource, int textViewResourceId, List<Track> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String imageURL = "";

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_track, null);
        }

        Track track = getItem(position);

        if (track != null) {
            ImageView album_image = (ImageView) v.findViewById(R.id.list_item_track_image);
            TextView album_name = (TextView) v.findViewById(R.id.list_item_album_name);
            TextView track_name = (TextView) v.findViewById(R.id.list_item_track_name);

            imageURL = track.album.images.get(0).url;

            if (album_image != null) {
                mPicasso.with(getContext()).load(imageURL).into(album_image);
            }

            if (album_name != null) {
                album_name.setText(track.album.name);
            }

            if (track_name != null) {
                track_name.setText(track.name);
            }
        }

        return v;
    }
}

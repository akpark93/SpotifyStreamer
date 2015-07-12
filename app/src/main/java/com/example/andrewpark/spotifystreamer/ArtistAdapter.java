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

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by andrewpark on 7/12/15.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    Picasso mPicasso;

    public ArtistAdapter(Context context, int resource, int textViewResourceId, List<Artist> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String imageURL = "";

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_artist, null);
        }

        Artist artist = getItem(position);

        if (artist != null) {
            ImageView artist_image = (ImageView) v.findViewById(R.id.list_item_artist_imageview);
            TextView artist_name = (TextView) v.findViewById(R.id.list_item_artist_textview);

            imageURL = artist.images.get(0).url;

            if (artist_image != null) {
                mPicasso.with(getContext()).load(imageURL).into(artist_image);
            }

            if (artist_name != null) {
                artist_name.setText(artist.name);
            }
        }

        return v;
    }
}

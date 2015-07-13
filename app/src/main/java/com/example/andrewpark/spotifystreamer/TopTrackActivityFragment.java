package com.example.andrewpark.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTrackActivityFragment extends Fragment {

    private static final String LOG_TAG = "TrackActivity";

    ArrayList<Track> trackList = new ArrayList<Track>();
    TrackAdapter mTrackAdapter;

    SpotifyApi api;
    SpotifyService service;

    public TopTrackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String spotifyID = "";

        View rootView = inflater.inflate(R.layout.fragment_top_track, container, false);

        Intent intent = getActivity().getIntent();
        if ((intent != null) && intent.hasExtra(Intent.EXTRA_TEXT)) {
            spotifyID = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.v(LOG_TAG, "Spotify ID: " + spotifyID);
        }
        getTrackList(spotifyID);

        mTrackAdapter = new TrackAdapter(getActivity(),R.layout.list_item_track,R.id.listview_tracks,trackList);
        ListView track_listView = (ListView) rootView.findViewById(R.id.listview_tracks);
        track_listView.setAdapter(mTrackAdapter);

        return rootView;
    }

    public void getTrackList(String spotifyID) {
        FetchTrackList trackTask = new FetchTrackList();
        trackTask.execute(spotifyID);
    }

    public class FetchTrackList extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... spotifyID) {
            api = new SpotifyApi();
            service = api.getService();
            Map<String, Object> options = new HashMap<>();
            options.put("country", "US");
            Log.v(LOG_TAG, "spotify id in doInBackground: " + spotifyID[0]);


            service.getArtistTopTrack(spotifyID[0], options, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    Log.v(LOG_TAG, "SUCCESS");
                    trackList.clear();
                    if (!tracks.tracks.isEmpty()) {
                        trackList.addAll(tracks.tracks);
                    } else {
                        Toast.makeText(getActivity(),"Sorry. No tracks found...", Toast.LENGTH_SHORT).show();
                    }
                    mTrackAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
            return null;
        }
    }
}

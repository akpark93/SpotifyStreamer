package com.example.andrewpark.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistActivityFragment extends Fragment {

    private static final String LOG_TAG = "SearchArtistFragment";

    ArrayList<Artist> artistList = new ArrayList<Artist>();
    ArtistAdapter mArtistAdapter;

    SpotifyApi api;
    SpotifyService service;

    public SearchArtistActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_artist, container, false);

        //set adapter
        mArtistAdapter = new ArtistAdapter(getActivity(), R.layout.list_item_artist, R.id.listview_artist, artistList);
        ListView artist_listView = (ListView) rootView.findViewById(R.id.listview_artist);
        artist_listView.setAdapter(mArtistAdapter);

        //retrieve artists while searching
        SearchView artistSearch = (SearchView) rootView.findViewById(R.id.searchview_artist);
        artistSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateArtistList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v(LOG_TAG, "query: " + newText);
                updateArtistList(newText);
                return true;
            }
        });

        return rootView;
    }


    //get artist list info.
    public void updateArtistList(String query) {
        FetchArtistList artistTask = new FetchArtistList();
        artistTask.execute(query);
    }

    //fetch artists in background using asynctask
    public class FetchArtistList extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... artist) {
            api = new SpotifyApi();
            service = api.getService();
            service.searchArtists(artist[0], new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    artistList.clear();
                    if (!artistsPager.artists.items.isEmpty()) {
                        artistList.addAll(artistsPager.artists.items);
                    } else {
                        Toast.makeText(getActivity(), "Artist not found. Please search again...", Toast.LENGTH_SHORT).show();

                    }
                    mArtistAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
            return null;
        }
    }
}

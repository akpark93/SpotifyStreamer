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

        //get artists while searching
        SearchView artistSearch = (SearchView) rootView.findViewById(R.id.searchview_artist);
        artistSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v(LOG_TAG,"query: " + query);
                updateArtistList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }


    //get artist list info.
    public void updateArtistList(String query) {
        Log.v(LOG_TAG, "Entered updateArtistList method with query: " + query);
        FetchArtistList artistTask = new FetchArtistList();
        artistTask.execute(query);
    }

    //fetch artists in background using asynctask
    public class FetchArtistList extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params) {
            api = new SpotifyApi();
            service = api.getService();
            Log.v(LOG_TAG, "artist name query : " + params[0]);
            service.searchArtists(params[0], new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    Log.v(LOG_TAG,"Success!");
                    artistList.clear();
                    artistList.addAll(artistsPager.artists.items);
                    mArtistAdapter.notifyDataSetChanged();
                    Log.v(LOG_TAG, "artists successfully obtained! and updated adapter!");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, "artists could not be obtained! no update in adapter!");
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mArtistAdapter.notifyDataSetChanged();
        }
    }
}

package com.compscitutorials.basigarcia.navigationdrawervideotutorial;


import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.co.recruit_lifestyle.android.widget.PlayPauseButton;

public class GalleryFragment extends Fragment {

    private List<Match> matchesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Match_Adapter mAdapter;

    View rootView;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        String user_id=getArguments().getString("user_id");
        String token=getArguments().getString("token");
        Log.d("Fragment","Gallery");
        //Get data from MainActivity
        //Log.d("FRAGMENT","Bundle "+strtext);
        // Inflate the layout for this fragment
        DBHandler db = new DBHandler(rootView.getContext());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mAdapter = new Match_Adapter(matchesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        for (Track t : db.getTracks(1)){
            matchesList.add(new Match(t.getTrackName(),t.getAlbumImage(),t.getArtistName(), t.getPreviewUrl()));
            Log.d("Matches-Name",t.getTrackName());
        }

        return rootView;
    }

    /*private void addMatches_list(JSONObject Tracks)throws JSONException{
        int i;
        Match match;
        String song_name;
        for (i=0 ; i < Tracks.getInt("total"); i++){
            JSONObject track_name = (JSONObject) Tracks.getJSONArray("items").get(i);
            track_name = (JSONObject) track_name.get("track");
            JSONObject track_img = (JSONObject) Tracks.getJSONArray("items").get(i);
            track_img = (JSONObject) track_img.get("track");
            //track_img = track_img.getJSONArray("images").getString("url");
            track_img = (JSONObject) track_img.getJSONObject("album").getJSONArray("images").get(1);
            String album_img_url = track_img.getString("url");
            //Log.d("TRACK64x64","--"+track_img.getString("url"));

            matchesList.add(new Match(track_name.getString("name"),album_img_url,track_name.getString("name")), track);
            mAdapter.notifyDataSetChanged();
        }
    }*/
}

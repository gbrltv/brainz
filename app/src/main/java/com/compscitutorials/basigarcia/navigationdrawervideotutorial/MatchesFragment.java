package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matheus on 07/10/2016.
 */
public class MatchesFragment extends Fragment{
    View rootView;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    DBHandler db;
    private int lastExpandedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.expandable_list, container, false);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData(getContext());
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        final String token = getArguments().getString("token");
        final String user_id = getArguments().getString("user_id");
        db = new DBHandler(getContext());
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastExpandedPosition != -1 && groupPosition != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }

                lastExpandedPosition = groupPosition;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if(childPosition == 0) {            //ADD TO PLAYLIST
                    Log.d("MatchesFragment","GroupPosition"+expandableListTitle.get(groupPosition));
                    Log.d("MatchesFragment","ArtistID:"+db.searchArtistID(expandableListTitle.get(groupPosition)));

                } else if(childPosition == 1) {     //DISCOVER ARTIST

                } else if(childPosition == 2) {     //REMOVE TRACK
                    db.updateStatus(expandableListTitle.get(groupPosition), 2);
                    //expandableListTitle.remove(groupPosition);
                }

                return false;
            }

        });

        return rootView;
    }



}

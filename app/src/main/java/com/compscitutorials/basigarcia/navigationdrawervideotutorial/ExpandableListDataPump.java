package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

/**
 * Created by Matheus on 07/10/2016.
 */
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData(Context context) {

        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        DBHandler db = new DBHandler(context);


        List<String> options = new ArrayList<String>();
        options.add("Add to Spotify");
        options.add("Discover Artist");
        options.add("Remove Track");
/*
        List<String> cricket = new ArrayList<String>();
        cricket.add("India");
        cricket.add("Pakistan");
        cricket.add("Australia");
        cricket.add("England");
        cricket.add("South Africa");

        List<String> football = new ArrayList<String>();
        football.add("Brazil");
        football.add("Spain");
        football.add("Germany");
        football.add("Netherlands");
        football.add("Italy");

        List<String> basketball = new ArrayList<String>();
        basketball.add("United States");
        basketball.add("Spain");
        basketball.add("Argentina");
        basketball.add("France");
        basketball.add("Russia");

        expandableListDetail.put("CRICKET TEAMS", cricket);
        expandableListDetail.put("FOOTBALL TEAMS", football);
        expandableListDetail.put("BASKETBALL TEAMS", basketball);
  */
        for (Track t : db.getTracks(1)){

            expandableListDetail.put(t.getTrackName(),options);
        }
        return expandableListDetail;
    }
}

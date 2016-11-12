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

        for (Track t : db.getTracks(1)){
            expandableListDetail.put(t.getId(),options);
        }
        return expandableListDetail;
    }
}

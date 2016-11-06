package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matheus on 01/07/2016.
 */
public class threadMatches extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... params) {
        try{
            Log.d("Async", "doInBackground: "+ params[0]);
            return getPlaylist(params[0],params[1]);
        }catch(Exception e){
            Log.d("Async","ERRO DENTRO ASYNC"+ e);
        }
        return null;
    }

    // HTTP GET request
    public JSONObject getPlaylist(String user_id,String accessToken) throws Exception {
        Log.d("AsyncThread", "sendGet: Chegou");
        String url = "https://api.spotify.com/v1/users/"+user_id+"/playlists";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = con.getResponseCode();
        Log.d("Matches","\nSending 'GET' request to URL : " + url);
        Log.d("Matches","Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonObj = new JSONObject(response.toString());

        return jsonObj;
    }
}

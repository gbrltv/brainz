package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matheus on 12/07/2016.
 */
public class threadrecommendations extends AsyncTask<String, Void, JSONObject> {
    protected JSONObject doInBackground(String... params) {
        try{
            Log.d("Async", "doInBackground: "+ params[0]);
            return getRecommendations(params[0]);
        }catch(Exception e){
            Log.d("Async","ERRO DENTRO ASYNC"+ e);
        }
        return null;
    }

    // HTTP GET request
    public JSONObject getRecommendations(String accessToken) throws Exception {
        Log.d("AsyncThread", "Recommendations");
        Log.d("TopArtists","Token"+accessToken);
        String url = "http://papayastudio.com.br/discovery/getRecommendations.php?token="+accessToken;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
       // con.setRequestProperty("token",accessToken);
        int responseCode = con.getResponseCode();
        Log.d("TopArtists","\nSending 'GET' request to URL : " + url);
        Log.d("TopArtists","Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Log.d("JsonRec","-"+response.toString());
        JSONObject jsonObj = new JSONObject(response.toString());

        return jsonObj;
    }
}

package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matheus on 23/05/2016.
 */
public class threadnelas extends AsyncTask<String, Void, JSONObject> {
    //Testar fora da mainactivity

   /* @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading Image ....");
        pDialog.show();

    }*/

    @Override
    protected JSONObject doInBackground(String... params) {
        try{
            Log.d("Async", "doInBackground: "+ params[0]);
            //getSongList(params[0]);
            return sendGet(params[0]);
        }catch(Exception e){
            Log.d("Async","ERRO DENTRO ASYNC"+ e);
        }
        return null;
    }

    // HTTP GET request
    public JSONObject sendGet(String accessToken) throws Exception {
        Log.d("AsyncThread", "sendGet: Chegou");
        String url = "https://api.spotify.com/v1/me";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = con.getResponseCode();
       // Log.d("MainActivity","\nSending 'GET' request to URL : " + url);
       // Log.d("MainActivity","Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonObj = new JSONObject(response.toString());

        //print result
        //Log.d("MainActivity","RESPOSTA STRING DO HTTP GET: "+response.toString());
        //Log.d("Async","RESPOSTA JSONOBJECT:" + jsonObj);
        //Log.d("Async","Email: "+jsonObj.get("email"));


        // return response.toString();
        return jsonObj;
    }
    public JSONObject getSongList(String accessToken) throws Exception{
        //Log.d("AsyncThread", "getSongList: Chegou");
        String url = "http://papayastudio.com.br/discovery/teste.php?token="+accessToken;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Authorization","token"+"BIXAO");

        int responseCode = con.getResponseCode();
        Log.d("Thread","RESPONSE"+responseCode);
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

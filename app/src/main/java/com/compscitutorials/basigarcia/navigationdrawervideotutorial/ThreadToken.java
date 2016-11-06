package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Spotify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Matheus on 12/09/2016.
 */
public class ThreadToken extends AppCompatActivity implements ConnectionStateCallback {

    String t;
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "2809677476c3414aabe43e45075048f6";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "customprotocol://callback";

    private static final int REQUEST_CODE = 1234;
    //Log.d("TOKEN","teste");
    AuthenticationResponse response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TOKEN","teste");
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming","user-read-private","playlist-read-private","playlist-read-collaborative","playlist-modify-private","streaming","user-follow-modify","user-follow-read", "user-read-birthdate", "user-read-email","playlist-read-private","user-top-read","user-library-read"});

        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("TOKEN","teste");
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.d("TOKEN","testetoken"+t);
                    t = response.getAccessToken();

                    break;
                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
    public String getToken(){
        return t;
    }
    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }
    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }
    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed: " + error);
    }
    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }
    @Override
    public void onConnectionMessage(String s) {
        Log.d("MainActivity", "Received connection message: " + s);
    }
    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }



}

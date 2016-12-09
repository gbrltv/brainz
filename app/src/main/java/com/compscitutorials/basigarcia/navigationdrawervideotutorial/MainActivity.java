package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ConnectionStateCallback {
    String auth_token_string;
    JSONObject User_info;
    AuthenticationResponse response;
    NavigationView navigationView = null;
    Toolbar toolbar = null;

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "2809677476c3414aabe43e45075048f6";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "customprotocol://callback";

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer
    private static final int REQUEST_CODE = 1234;

    CircleImageView iv;

    String profile_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the fragment initially
        Login fragment = new Login();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();
        getSupportActionBar().setTitle("Brainz");

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming","user-read-private","playlist-read-private","playlist-read-collaborative","playlist-modify-private","streaming","user-follow-modify","user-follow-read", "user-read-birthdate", "user-read-email","playlist-read-private","user-top-read","user-library-read"});
        //builder.setShowDialog(true);
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    // Log.d("MainActivity", "DEBUGSONTHETABLE =========>>> " + response.getAccessToken());
                    String[] ary = new String[]{response.getAccessToken()};
                    try{
                        // Thread GetUserInfo
                        // auth_token_string = settings.getString("TOKEN", "");
                        // Log.d("Main","Token--"+auth_token_string);
                        // String[] ary = new String[]{};
                        MainFragment fragment = new MainFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        Bundle bundle=new Bundle();
                        //Passar Dados para Fragment...

                        try {
                            bundle.putString("token", response.getAccessToken());
                            bundle.putString("user_id", User_info.get("id").toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();

                        User_info = new threadnelas().execute(ary).get();
                        // Log.d("USER INFO","--"+User_info);

                        //Altera Dados da View
                        View headerView = navigationView.getHeaderView(0);
                        TextView emailText = (TextView) headerView.findViewById(R.id.email);
                        emailText.setText(User_info.get("email").toString());
                        TextView usernameText = (TextView) headerView.findViewById(R.id.username);
                        usernameText.setText(User_info.get("display_name").toString());

                        JSONObject profile_image_object = (JSONObject) User_info.getJSONArray("images").get(0);
                        profile_url = profile_image_object.getString("url");
                        iv = (CircleImageView) findViewById(R.id.profile_image);
                        LoadImageFromURL loadImage = new LoadImageFromURL();
                        loadImage.execute(profile_url);

                    }catch(Exception e){
                        Log.d("MainActivity","Erro ========> "+e);
                    }
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            //Set the fragment initially
            MainFragment fragment = new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            Bundle bundle=new Bundle();
            //Passar Dados para Fragment...

            try {
                bundle.putString("token", response.getAccessToken());
                bundle.putString("user_id", User_info.get("id").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_gallery) {
            //Set the fragment initially
            MatchesFragment fragment = new MatchesFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            Bundle bundle=new Bundle();

            //Passar Dados para Fragment...
            try {
                bundle.putString("user_id", User_info.get("id").toString());
                bundle.putString("token", response.getAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } //else if (id == R.id.nav_slideshow) {

        //

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }
    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    public class LoadImageFromURL extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                //Log.d("LoadImageFromURLThread","Param: "+params[0]);
                URL url = new URL(params[0]);

                InputStream is = url.openConnection().getInputStream();
                Bitmap bitMap = BitmapFactory.decodeStream(is);

                return bitMap;

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            if(result != null) {
                super.onPostExecute(result);
                iv.setImageBitmap(result);
            }else {
                Log.d("ProfileThread","Problemas de conexao");
            }
        }

    }
}

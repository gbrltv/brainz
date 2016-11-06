package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

/**
 * Created by Matheus on 12/09/2016.
 */
public class LoginActivity extends AppCompatActivity{
    Button Login;
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
        setContentView(R.layout.login);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming","user-read-private","playlist-read-private","playlist-read-collaborative","playlist-modify-private","streaming","user-follow-modify","user-follow-read", "user-read-birthdate", "user-read-email","playlist-read-private","user-top-read","user-library-read"});

        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        //Login = (Button) findViewById(R.id.btLogin);
        //Login.setOnClickListener(new View.OnClickListener() {

           // @Override
          //  public void onClick(View view) {
          //      Log.d("LOGIN","Loganelas");
//                ThreadToken token = new ThreadToken();
  //              t = token.getToken();
    //            Log.d("LOGIN","---"+t);
           // }
        //});
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
                    t = response.getAccessToken();
                    Log.d("TOKEN","testetoken"+t);
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("TOKEN", t);
                    editor.commit();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
}

package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matheus on 13/07/2016.
 */
public class Login extends Fragment {
    View rootView;

    public Login() {
        // Required empty public constructor
    }
    //Log.d("Login","Login");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.login, container, false);

        // Log.d("Login","ChegouAQui");
        return rootView;
    }
}

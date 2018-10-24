package com.example.krzysztof.foodfinder.Controller;


import android.content.Context;
import android.os.AsyncTask;

import android.view.View;


import com.example.krzysztof.foodfinder.Model.Place;
import com.example.krzysztof.foodfinder.View.MainActivity;

import java.util.ArrayList;



public class GooglePlacesAPI extends AsyncTask<View, Void, String> {



    final String GOOGLE_KEY = "AIzaSyBCwRcm3DZfcyEAXX8STiO1LzquZlOjVy8";
    String latitude;
    String longtitude;
    String temp;
    public ArrayList<Place> placesList;
    Context context;
    GooglePlacesInterface placesInterface;
    String searchRadius;


    public GooglePlacesAPI(double latitude, double longtitude, Context context, String radius) {

        this.latitude = latitude+"";
        this.longtitude = longtitude+"";
        this.context= context;
        placesInterface= (GooglePlacesInterface) context;
        searchRadius= radius.trim();
    }

    @Override
    public String doInBackground(View... views) {

        String myURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longtitude+"&radius="+searchRadius+"&keyword=jedzenie&key="+GOOGLE_KEY;
        // make Call to the url
        temp= placesInterface.makeCall(myURL);
        //print the call in the console
        System.out.println(myURL);
        return "";
    }

    @Override
    protected void onPostExecute(String s) {

        if(temp != null)
        {
            placesList= placesInterface.parseGooglePlaces(temp);
            MainActivity ma= (MainActivity) context;
            ma.setUpAdapterAndView(placesList);
        }
    }
}

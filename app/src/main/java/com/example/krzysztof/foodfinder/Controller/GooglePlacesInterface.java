package com.example.krzysztof.foodfinder.Controller;

import com.example.krzysztof.foodfinder.Model.Place;

import java.util.ArrayList;

public interface GooglePlacesInterface {

    String makeCall(String url);
    ArrayList<Place> parseGooglePlaces(final String response);
}

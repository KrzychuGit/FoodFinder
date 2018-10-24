package com.example.krzysztof.foodfinder.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.krzysztof.foodfinder.Controller.CustomAdapter;

import com.example.krzysztof.foodfinder.Controller.GooglePlacesAPI;
import com.example.krzysztof.foodfinder.Controller.GooglePlacesInterface;
import com.example.krzysztof.foodfinder.Model.Place;
import com.example.krzysztof.foodfinder.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements LocationListener, GooglePlacesInterface {

    private TextView tvAddress;

    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT= 0;
    public LocationManager manager;
    public String addressName="";
    public double myLongitude, myLatitude;

    RecyclerView recyclerView;
    LayoutInflater layoutInflater;
    ArrayList<Place> listOfPlaces;
    CustomAdapter adapter;
    private final int SETTINGS =1;
    String searchRadius;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchRadius= "10000";
        adapter= new CustomAdapter( layoutInflater, listOfPlaces, this);
        tvAddress= findViewById(R.id.tvAddress);
        recyclerView= findViewById(R.id.recyclerView);
       layoutInflater= getLayoutInflater();
        listOfPlaces= new ArrayList<>();

        findUserLocalization();

        ActionBar actionBar= getSupportActionBar();

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.settings:
                Intent intent= new Intent(this, SettingsActivity.class);
                intent.putExtra("currentRadius", searchRadius);
                startActivityForResult(intent, SETTINGS);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== SETTINGS && resultCode== RESULT_OK)
        {
            String rad= data.getStringExtra("radius");

            searchRadius= rad;
            new GooglePlacesAPI(myLatitude, myLongitude, this, searchRadius).execute();
            Toast.makeText(this, "WORKS RADIUS!", Toast.LENGTH_SHORT).show();
        }
    }


    void findUserLocalization()
    {
        tvAddress.setText("Wyszukiwanie...");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                getLocation();
            }
            else
            {
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(MainActivity.this, "Application required to access location", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_RESULT);

            }
        }
        else
        {
            getLocation();
        }
    }



    public void getLocation(){

        try
        {
            manager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, this);

        }
        catch (SecurityException e){
            Toast.makeText(this, "Wyjątek!! get location", Toast.LENGTH_SHORT).show();
        }
    }


    public void setUpAdapterAndView(ArrayList<Place> listOfData) {

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        this.listOfPlaces= listOfData;
        recyclerView.setLayoutManager(layoutManager);
        adapter= new CustomAdapter(  layoutInflater, listOfPlaces, this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration= new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        MainActivity.this,
                        R.drawable.drawable_white
                )
        );

        recyclerView.addItemDecoration(itemDecoration);

        /*ItemTouchHelper itemTouchHelper= new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);*/
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode== REQUEST_PERMISSION_FINE_LOCATION_RESULT)
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Application will not be run without location permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        try
        {
            Geocoder geocoder= new Geocoder(this, Locale.getDefault());
            List<Address> addresses= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);

            addressName=addresses.get(0).getAddressLine(0);

            myLatitude= location.getLatitude();
            myLongitude= location.getLongitude();
        }catch(Exception e){}

        tvAddress.setText(addressName);

        new GooglePlacesAPI(myLatitude, myLongitude, this, searchRadius).execute();
        setUpAdapterAndView(listOfPlaces);
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

        Toast.makeText(this, "GPS IS DISABLED!", Toast.LENGTH_LONG).show();
        tvAddress.setText("UNKNOWN");
    }



    @Override
    public String makeCall(String url)
    {

        StringBuffer buffer_string= new StringBuffer(url);
        String replyString = "";

        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return replyString.trim();
    }

    @Override
    public ArrayList<Place> parseGooglePlaces(final String response) {

        ArrayList<Place> temp = new ArrayList<Place>();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {

                    if (jsonArray.getJSONObject(i).has("types")) {
                        JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                        Place poi = new Place();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).optString("name")+" <--");
                            poi.setLogoURL(jsonArray.getJSONObject(i).optString("icon", " "));
                        }
                        if (jsonArray.getJSONObject(i).has("opening_hours")) {

                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                poi.setOpen("OPEN");
                            } else {
                                poi.setOpen("CLOSED");
                            }
                        }
                        else
                            poi.setOpen("UNKNOWN");

                        if(jsonArray.getJSONObject(i).has("vicinity"))
                        {
                            poi.setAddress(jsonArray.getJSONObject(i).optString("vicinity"));
                        }
                        temp.add(poi);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Place>();
        }
        return temp;
    }
}
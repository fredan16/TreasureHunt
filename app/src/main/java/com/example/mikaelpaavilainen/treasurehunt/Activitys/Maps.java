package com.example.mikaelpaavilainen.treasurehunt.Activitys;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mikaelpaavilainen.treasurehunt.HTTPREQUEST.HttpRequest;
import com.example.mikaelpaavilainen.treasurehunt.R;
import com.example.mikaelpaavilainen.treasurehunt.class_event.UserPos;
import com.example.mikaelpaavilainen.treasurehunt.class_event.Event;
import com.example.mikaelpaavilainen.treasurehunt.database.Repository;
import com.example.mikaelpaavilainen.treasurehunt.database.eventList;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class Maps extends FragmentActivity implements OnMapReadyCallback {


    private boolean debug = true;
    private boolean canChange = true;
    private boolean loadedEvents = false;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    UserPos user = new UserPos();
    private static final LatLng Sweden = new LatLng(58.39703366, 13.87653310);
    Event[] e; //first test event
    //Event  userCircle; //user circle
    private Context context;

    float zoomValue;

    LinearLayout linearLayout;
    Button eventButton;

    public int Score = 0;
    public TextView ScoreText;
    public String username = "";
    public String userhash = "";
    private Random r;

    int natuiskMile = 1852;

    MediaPlayer mp;
    Event userCircle;

    //Room Databas
    Repository database;
    List<eventList> Downloaded_event;

    //toggle follow button
    ToggleButton tb;
    boolean followPlayer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.context = this;
        this.username = "";
        this.userhash = "";
        Intent thiz = getIntent();
        Bundle b = thiz.getExtras();
        if (b != null) {
            this.Score += b.getInt("newScore");
            username = b.getString("name");

            userhash = b.getString("hash");
            //username += " " +userhash;
        }
        mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.ev);

        database = new Repository();
        database.onCreate(this);
        database.getDB().eventListDao().deleteAll();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //check if databse is empty
        if (database.getDB().eventListDao().countEvents() == 0) {
            Log.d("HTTPSJ", "onCreate: Empty database");
            //check for internet
            if (cm.getActiveNetworkInfo() != null) {
                Log.d("HTTPSJ", "onCreate: Has Internet");

                //check if user has events
                database.getDB().eventListDao().deleteAll();
                //load new events if database is empty and connected to internet
                //get all events and add them to internal database
                JSONObject j;
                HttpRequest events = new HttpRequest();
                events.setValues("http://192.168.1.2/TreasureHunt/get_event.php", "", context);
                events.execute();

                while (events.respons.equals("")) {
                    //Nothing
                    Log.d("HTTPSJ", "onLocationChanged: Not Yet");
                }
                //change score

                //set events
                Log.d("HTTPSJ", "onCreate: " + events.respons);
                try {
                    j = new JSONObject(events.respons);
                    Log.d("HTTPSJ", "onCreate: " + j.length());
                    e = new Event[j.length()];
                    for (int i = 0; i < j.length(); i++) {
                        //Log.d("HTTPSJ", "onLocationChanged: " );
                        JSONObject temp = new JSONObject(j.getString("" + i));
                        //add to database
                        eventList addToDataBaseTemp = new eventList();
                        addToDataBaseTemp.setJson(temp.toString());
                        database.getDB().eventListDao().insert(addToDataBaseTemp);
                        Log.d("HTTPSJ1", "onCreate: Inserted test");
                        Downloaded_event = database.getDB().eventListDao().getAll();
                        Log.d("HTTPSJ1", "onCreate: ID:" + Downloaded_event.get(i).id + " - " + Downloaded_event.get(i).json);
                    }
                } catch (Exception e) {
                    Log.e("HTTPSJ", "onLocationChanged: ", e);
                }
            }
            //Print error msg cant download events
            else {
                Toast.makeText(context, "Cant Download Events, Please Connect To Internet", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("HTTPSJ", "onCreate: Loaded from database");
            e = new Event[database.getDB().eventListDao().countEvents()];
            Downloaded_event = database.getDB().eventListDao().getAll();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Result1", "onActivityResult: Gott result");
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("Result1", "onActivityResult: Gott confirmed results :" + Score + " - " + data.getIntExtra("newScore", 1));
                int newScore = data.getIntExtra("newScore", 1);
                Score += newScore;
                Log.d("Result1", "onActivityResult: " + Score);
                canChange = true;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    //When Back button is pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Debug stuff
        // Add a marker in sweden and move the camera
        LatLng sweden = Sweden;
        //Google map Init
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        //mMap.addMarker(new MarkerOptions().position(sweden).title("Marker in Sweden"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sweden));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sweden.latitude, sweden.longitude), 12.0f));
        //Create event button
        eventButton = new Button(context);
        eventButton.setText("Öppna Event");
        addContentView(eventButton, new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        eventButton.setVisibility(View.GONE);
        eventButton.setX(650);
        eventButton.setPadding(250, 50, 250, 50);
        //random int generator
        r = new Random(); // 58.397455,13.875244
        //Creates a grid for events - i think
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //users location
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        Log.d("GMAP", "initGPS: " + locationManager);
        //Check if gps is turned on
        boolean gpss = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        locationListener = new MyLocationlistener();
        if (debug) {
            if (gpss == true) {
                Log.d("GMAP", "onMapReady: GPS ON");
            } else {
                Log.d("GMAP", "onMapReady: GPS Off");
            }
            Log.d("GMAP", "onMapReady: Fine -" + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
            Log.d("GMAP", "onMapReady: internet - " + ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET));
        }
        //Check if app has permission to use internet and gps.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        } else {
            Log.d("GMAP", "onMapReady: Granted");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        Log.d("GMAP3", "onMapReady: Loading Events");

        //call on my method here
        Log.d("GMAP3", "onMapReady: Loading Methods");
        //sets current location on map
        mMap.setMyLocationEnabled(true);
        //Creates a test event circle
        userCircle = new Event(context);
        userCircle.init(58.397125, 13.876750, "user", 1, 360, mMap, 10, Color.BLUE, Color.GRAY, 2, "Player", "You Are A What?", 50); // event it
        userCircle.onDraw();
        //creates texview and prints user score
        ScoreText = new TextView(this);//add textview to canvas
        ScoreText.setText("Score " + Score);
        ScoreText.setX(0); //x position
        ScoreText.setY(0);//y position
        ScoreText.setTextSize(58); //font size
        ScoreText.setTextColor(Color.WHITE);
        addContentView(ScoreText, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        //prints username
        TextView textView2 = new TextView(this);
        textView2.setText("User " + username);
        textView2.setY(100);
        textView2.setTextSize(40);
        textView2.setTextColor(Color.WHITE);
        addContentView(textView2, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        //creates toggle button for following user or not
        tb = new ToggleButton(this);
        tb.setTextOff("Start Follow");
        tb.setTextOn("Stop Follow");
        tb.setX(1550);
        tb.setY(10);
        tb.setTextSize(20);
        addContentView(tb, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                else{
                    if(isChecked){
                        followPlayer = true;
                    }
                    else{
                        followPlayer = false;
                    }
                }
            }
        });
        /*
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


            }
        });
        */
        Log.d("GMAP3", "onMapReady: Checking internet");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {

            //http send position to webpage
            HttpRequest userPos = new HttpRequest();
            String urlParameters = "";
            userPos.setValues("http://192.168.1.2/TreasureHunt/pos_update.php", urlParameters, context);
            userPos.execute();
            Log.d("GMAP3", "onMapReady: Sent Position");

        }
    }

    public class MyLocationlistener implements LocationListener {


        public double lat;
        public double lng;

        @Override
        public void onLocationChanged(Location location) {
            Log.d("GMAP3", "onLocationChanged: Looking at you >:)");
            if (location != null) {
                ScoreText.setText("Score " + Score);
                Log.d("Result1", "onLocationChanged: " + Score);
                lat = location.getLatitude();
                lng = location.getLongitude();
                if (debug) {
                    Log.d("GMAPL", "onLocationChanged: " + lat);

                    Log.d("GMAPL", "onLocationChanged: " + lng);
                }

                //userCircle.setPosition(lat,lng); //makes circle go around user
                user.addPosition(lat, lng); //for calculating  person position

                if(followPlayer == true){
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    else{

                            Log.d("GMAP123", "onCheckedChanged: Toggled on");
                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat,lng));
                            CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);

                            mMap.moveCamera(center);
                            mMap.animateCamera(zoom);

                    }
                }
                double latMin = 0;
                double latMax = 0;
                double lngMin = 0;
                double lngMax = 0;
                if (!loadedEvents && user.startCheck) {
                    Log.d("GMAP5", "onLocationChanged: Started Adding Events");
                    try {
                        for (int i = 0; i < Downloaded_event.size(); i++) {
                            Log.d("HTTPSJ", "onLocationChanged: EventList Size - " + Downloaded_event.size());
                            Log.d("HTTPSJ", "onLocationChanged: json string - " + Downloaded_event.get(i).json);
                            //Reads from internal database
                            JSONObject temp = new JSONObject(Downloaded_event.get(i).json);

                            JSONObject temp2 = new JSONObject(temp.getString("event"));
                            Log.d("HTTPSJ", "onLocationChanged: " + temp2 + " | " + temp2.getString("name"));


                            latMin = user.ySum - 0.01;
                            latMax = user.ySum + 0.01;
                            lngMin = user.xSum - 0.01;
                            lngMax = user.xSum + 0.01;

                            double randomLng = lngMin + r.nextDouble() * (lngMax - lngMin);
                            double randomLat = latMin + r.nextDouble() * (latMax - latMin);
                            e[i] = new Event(context);
                            e[i].init(randomLng, randomLat, temp2.getString("name"), temp2.getInt("level"), temp2.getInt("timercount"), mMap, temp2.getInt("size"), Color.BLACK, Color.RED, temp2.getInt("width"), temp2.getString("answer"), temp2.getString("problem"), temp2.getInt("worth")); //first event
                            e[i].onDraw();
                            Log.d("HTTPSJ", "onMapReady: " + i + " || " + temp2.getString("name") + ": " + randomLng + " - " + randomLat + " | " + user.xSum + " - " + user.ySum);

                            loadedEvents = true;
                        }
                    } catch (Exception e) {
                        Log.e("GMAP5", "onLocationChanged: ", e);
                    }
                }

                zoomValue = mMap.getCameraPosition().zoom;

                //Log.d("GMAP2", "onMapReady: " + zoomValue);

                if (user.startCheck && loadedEvents){
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(cm.getActiveNetworkInfo() != null){

                        HttpRequest userPos = new HttpRequest();
                        String urlParameters ="hash=" + userhash + "&posx=" + lat +"&posy=" + lng;
                        userPos.setValues("http://192.168.1.2/TreasureHunt/pos_update.php",urlParameters,context);
                        userPos.execute();

                    }

                    for (final Event ae: e) {
                        Location eventLocation = new Location(LocationManager.GPS_PROVIDER);
                        //Hypotenusan
                        eventLocation.setLatitude(ae.getCenter().latitude);
                        eventLocation.setLongitude(ae.getCenter().longitude);



                       Log.d("GMAP4", "onLocationChanged: Lenght <->" +(location.distanceTo(eventLocation)*natuiskMile) + " <= " + ae.eventHitbox());
                        if ((location.distanceTo(eventLocation)*natuiskMile) <= (ae.eventHitbox())){ //less than middle point + width && more than middle point
                            ae.collided = true;

                            if(ae.collided && !ae.createdButton){


                                mp.start();
                                ae.createdButton = true;
                                Log.d("GMAP4", "onLocationChanged: Button Created " +ae.name);

                                eventButton.setVisibility(View.VISIBLE);

                                eventButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub

                                        if (canChange){
                                            Intent nIn = ae.startEvent();
                                            nIn.putExtra("username",username);
                                            nIn.putExtra("userhash",userhash);
                                            startActivityForResult(nIn,1);

                                            canChange = false;

                                        }

                                    }
                                });
                            }


                            if (debug){
                                Log.d("GMAP2", "onMapReady: Inside " + ae.getCenter().latitude + ae.getwidth() + " || " + user.xSum );
                                Log.d("GMAP2", "onMapReady: Inside2 " + ae.getCenter().longitude + ae.getwidth() + " || " + user.ySum );
                            }

                            //inside event radius - check if they click event

                        }
                        else{
                            if(debug){
                                Log.d("GMAP2", "onMapReady: Not Inside " + ae.getCenter().latitude + ae.getwidth() + " || " + user.xSum );
                                Log.d("GMAP2", "onMapReady: Not Inside2 " + ae.getCenter().longitude + ae.getwidth() + " || " + user.ySum );
                                ae.collided = false;

                            }
                            if(ae.createdButton){
                                eventButton.setVisibility(View.GONE);
                                ae.createdButton = false;
                                mp.stop();

                            }
                        }
                    }
                }
                Log.d("GMAP", "onLocationChanged: No Location");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}

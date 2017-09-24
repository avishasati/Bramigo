package project.reaper.mapper;

/**
 * Created by Reaper on 14/9/17.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * The first activity that you see.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // The Profile class. Will be used to get the user data fields.
    Profile userProfile;
    GoogleMap map;

    // The three additional control buttons on the map.
    ImageButton emergencyCall;
    ImageButton viewProfile;
    ImageButton viewLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.view_map);
        mapFragment.getMapAsync(this);

        // Instantiating the Profile class of the User.
        userProfile = ((AppValues) this.getApplication()).getUserProfile();

        emergencyCall = (ImageButton) findViewById(R.id.img_btn_call);
        viewProfile = (ImageButton) findViewById(R.id.img_btn_view_profile);
        viewLocal = (ImageButton) findViewById(R.id.img_btn_view_local);

        // What to do when the emergency call button is pressed?
        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall("+917795144747"); // Change this to the Hospital number for Emergency Calls.
            }
        });

        // What to do when the view profile button is pressed?
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this,  ProfileActivity.class);
                // Here -1 means User. All values > 0 will be for the list of markers.
                intent.putExtra("Profile Index", -1);
                startActivity(intent);
            }
        });

        // For smooth camera transition.
        viewLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userProfile.getLocation(), 10));
            }
        });

        // The name explains itself!
        addDummyData();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        // Get all the available profiles
        List<Profile> profiles = ((AppValues) this.getApplication()).getProfileList();

        // Adding all the markers.
        for(int i = 0; i < profiles.size(); i++) {
            Profile px = profiles.get(i);
            px.addTrackerRate(8);
            addMarker(px, googleMap);
        }

        // Adds user marker
        // TODO: Change Marker Icon USER
        if (userProfile.getTrackerRate() > 7){
            googleMap.addMarker(new MarkerOptions().position(userProfile.getLocation())
                    .title(userProfile.getName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));

        }else if (userProfile.getTrackerRate() > 4) {
            googleMap.addMarker(new MarkerOptions().position(userProfile.getLocation())
                    .title(userProfile.getName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gold_icon));

        }else{
            googleMap.addMarker(new MarkerOptions().position(userProfile.getLocation())
                    .title(userProfile.getName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green_icon));

        }

        // This method here is setting the view so that the whole map is available.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.2618911,  82.5490325)));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(4));

        googleMap.setOnMarkerClickListener(this);
    }

    // This function assigns markers based on the tracker rate.
    void addMarker (Profile px, GoogleMap googleMap) {
        if (px.isDoctor) {
            // TODO: Change Marker Icon DOCTOR
            googleMap.addMarker(new MarkerOptions().position(px.getLocation())
                    .title(px.getName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon));
        }
        else {
            if (px.getTrackerRate() > 7) {
                // TODO: Change Marker Icon RED
                googleMap.addMarker(new MarkerOptions().position(px.getLocation())
                        .title(px.getName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));
            } else if (px.getTrackerRate() > 4) {
                // TODO: Change Marker Icon YELLOW
                googleMap.addMarker(new MarkerOptions().position(px.getLocation())
                        .title(px.getName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gold_icon));
            } else {
                // TODO: Change Marker Icon GREEN
                googleMap.addMarker(new MarkerOptions().position(px.getLocation())
                        .title(px.getName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green_icon));
            }
        }
    }

    // What to do when a marker is clicked?
    @Override
    public boolean onMarkerClick(Marker marker) {
        float zoom = map.getCameraPosition().zoom;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoom));

        // Calling the information dialog
        int index = getIndex(marker.getTitle(), marker.getPosition());
        if (index != -1) {
            ((AppValues) getApplication()).setIndex(index);
            DialogFragment newFragment = new Dialog();
            newFragment.show(getSupportFragmentManager(), "Dialog");
        }
        return false;
    }

    // The marker identification is done via the index number.
    int getIndex(String name, LatLng position) {
        int index = -1;
        List<Profile> profiles = ((AppValues) this.getApplication()).getProfileList();

        for(int i = 0; i < profiles.size(); i++) {
            Profile px = profiles.get(i);
            if (px.getName().equals(name) && px.getLocation().equals(position)) {
                index = i;
                break;
            }
        }

        return index;
    }

    // Method to make a phonecall.
    void makePhoneCall (String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ phoneNumber));

        if (ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
    }

    // Really? Does this need explanation?
    void addDummyData () {
        userProfile = new Profile("Avisha Sati(User)", "+917795144747", 23, 68, false);
        userProfile.setLocation(16.7511112, 77.7614061);

        Profile profile1 = new Profile("Issac Clarke", "+910000000000", 22, 70, false);
        profile1.setLocation(12.8211112, 77.8614061);

        Profile profile3 = new Profile("Ellie Langford", "+910000000000", 22, 70, false);
        profile3.setLocation(13.2211112, 77.6614061);

        Profile profile4 = new Profile("Nolan Strauss", "+910000000000", 22, 70, true);
        profile4.setLocation(12.2211112, 78.6614061);

        List<Profile> profileList = new ArrayList<>();

        profileList.add(profile3);
        profileList.add(profile1);
        profileList.add(profile4);

        ((AppValues) this.getApplication()).setProfileList(profileList);
        ((AppValues) this.getApplication()).setUserProfile(userProfile);
    }

    void addProfileData (String name, String phoneNumber, int age, int weight, boolean isDoctor, double lat, double lng) {
        Profile px = new Profile(name, phoneNumber, age, weight, isDoctor);
        px.setLocation(lat, lng);
        ((AppValues) this.getApplication()).addProfile(px);
    }
}
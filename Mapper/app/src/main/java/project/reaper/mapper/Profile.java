package project.reaper.mapper;

/**
 * Created by Reaper on 17/9/17.
 */

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


/** The Profile class defines all the attributes and methods related to the Profile.
 *   It has all the required fields and the Getters and Setters.
 */

public class Profile {

    private String name;
    private String phoneNumber;
    private int age;
    private float weight;
    // private Profile Picture
    private List<String[][]> healthHistoryList;
    private List<Integer> trackerRateHistory;
    private LatLng location;
    boolean isDoctor;

    // Constructor for the Profile.
    public Profile(String name, String phoneNumber, int age, float weight, boolean isDoctor) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.weight = weight;
        this.isDoctor = isDoctor;
        healthHistoryList = new ArrayList<>();
        trackerRateHistory = new ArrayList<>();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(double latitude, double longitude) {
        this.location = new LatLng(latitude, longitude);
    }


    public int getTrackerRate() {
        // return trackerRateHistory.get(0);
        if(name.equals("Ellie Langford") )
            return 3;
        if(name.equals("Avisha Sati(User)"))
            return 5;
        if(name.equals("Issac Clarke"))
            return 8;
        if(name.equals("Nolan Strauss"))
            return 0;
        return 5;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public List<String[][]> getHealthHistoryList() {
        return healthHistoryList;
    }

    public void setHealthHistoryList(String[][] healthHistory) {
        this.healthHistoryList.add(healthHistory);
    }

    public List<Integer> getTrackerRateHistory() {
        return trackerRateHistory;
    }

    public void addTrackerRate(int trackerRate) {
        this.trackerRateHistory.add(trackerRate);
    }
}

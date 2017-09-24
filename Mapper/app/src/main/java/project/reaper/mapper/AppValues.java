package project.reaper.mapper;

import android.app.Application;

import java.util.List;

/**
 * Created by Reaper on 17/9/17.
 */

/** This class is used to communicate index values between different activities.
 *   Mostly consists of Getters and Setters.
 */
public class AppValues extends Application{

    int index = -1;
    Profile userProfile;
    private List<Profile> profileList;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if (index >= -1) {
            this.index = index;
        }
    }

    public Profile getUserProfile() {
        return userProfile;
    }

    public Profile getProfile(int index) {
        if (index == -1) {
            return getUserProfile();
        }
        else {
            return profileList.get(index);
        }
    }

    public void setUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public void addProfile (Profile profile) {
        profileList.add(profile);
    }
}


package com.karine.go4lunch.models.NearbySearchAPI;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Geometry {

    @SerializedName("location")
    private Location mLocation;

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

}

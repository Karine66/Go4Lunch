
package models.PlaceDetailsAPI;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class PlaceDetailsResult implements Serializable, Parcelable {

    @SerializedName("name")
    private String mName;
    @SerializedName("opening_hours")
    private OpeningHours mOpeningHours;
    @SerializedName("photos")
    private List<Photo> mPhotos;
    @SerializedName("rating")
    private Double mRating;
    @SerializedName("vicinity")
    private String mVicinity;
    @SerializedName("formatted_phone_number")
    private String mFormattedPhoneNumber;
    @SerializedName("geometry")
    private Geometry mGeometry;
    @SerializedName("website")
    private String mWebsite;


    protected PlaceDetailsResult(Parcel in) {
        mName = in.readString();
        if (in.readByte() == 0) {
            mRating = null;
        } else {
            mRating = in.readDouble();
        }
        mVicinity = in.readString();
        mFormattedPhoneNumber = in.readString();
        mWebsite = in.readString();
    }

    public static final Creator<PlaceDetailsResult> CREATOR = new Creator<PlaceDetailsResult>() {
        @Override
        public PlaceDetailsResult createFromParcel(Parcel in) {
            return new PlaceDetailsResult(in);
        }

        @Override
        public PlaceDetailsResult[] newArray(int size) {
            return new PlaceDetailsResult[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public OpeningHours getOpeningHours() {
        return mOpeningHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        mOpeningHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public Double getRating() {
        return mRating;
    }

    public void setRating(Double rating) {
        mRating = rating;
    }

    public String getVicinity() {
        return mVicinity;
    }

    public void setVicinity(String vicinity) {
        mVicinity = vicinity;
    }

    public String getFormattedPhoneNumber() {
        return mFormattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        mFormattedPhoneNumber = formattedPhoneNumber;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(Geometry geometry) {
        mGeometry = geometry;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        if (mRating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(mRating);
        }
        parcel.writeString(mVicinity);
        parcel.writeString(mFormattedPhoneNumber);
        parcel.writeString(mWebsite);
    }
}



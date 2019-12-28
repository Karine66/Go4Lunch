
package models;

import java.util.List;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GoogleApi {

    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("results")
    private List<Result> mResults;
    @SerializedName("status")
    private String mStatus;

    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    @Override
    public String toString() {
        return "GoogleApi{" +
                "mHtmlAttributions=" + mHtmlAttributions +
                ", mResults=" + mResults +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }
}

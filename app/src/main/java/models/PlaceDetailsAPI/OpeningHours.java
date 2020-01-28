
package models.PlaceDetailsAPI;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.time.LocalTime;

import java.util.Arrays;
import java.util.List;


//public class OpeningHours {
//
//    @SerializedName("open_now")
//    private Boolean mOpenNow;
//    @SerializedName("periods")
//    private List<Period> mPeriods;
//    @SerializedName("weekday_text")
//    private List<String> mWeekdayText;
//
//    public Boolean getOpenNow() {
//        return mOpenNow;
//    }
//
//    public void setOpenNow(Boolean openNow) {
//        mOpenNow = openNow;
//    }
//
//    public List<Period> getPeriods() {
//        return mPeriods;
//    }
//
//    public void setPeriods(List<Period> periods) {
//        mPeriods = periods;
//    }
//
//    public List<String> getWeekdayText() {
//        return mWeekdayText;
//    }
//
//    public void setWeekdayText(List<String> weekdayText) {
//        mWeekdayText = weekdayText;
//    }

public class OpeningHours implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Whether the place is open at the current time.
     *
     * <p>Note: this field will be null if it isn't present in the response.
     */
    @SerializedName("open_now")
    public Boolean openNow;

    public Boolean getOpenNow() {
      return openNow;
    }

    /** The opening hours for a Place for a single day. */
//    public static class Period implements Serializable {
//
//        private static final long serialVersionUID = 1L;
//
//        public static class OpenClose implements Serializable {
//
//            private static final long serialVersionUID = 1L;
//
//            public enum DayOfWeek {
//                SUNDAY("Sunday"),
//                MONDAY("Monday"),
//                TUESDAY("Tuesday"),
//                WEDNESDAY("Wednesday"),
//                THURSDAY("Thursday"),
//                FRIDAY("Friday"),
//                SATURDAY("Saturday"),
//
//                /**
//                 * Indicates an unknown day of week type returned by the server. The Java Client for Google
//                 * Maps Services should be updated to support the new value.
//                 */
//                UNKNOWN("Unknown");
//
//                private DayOfWeek(String name) {
//                    this.name = name;
//                }
////
//                private final String name;
//
//                public String getName() {
//                    return name;
//                }
//            }
//
//            /** Day that this Open/Close pair is for. */
//            public Period.OpenClose.DayOfWeek day;
//
//            /** Time that this Open or Close happens at. */
//            public LocalTime time;
//
//            @Override
//            public String toString() {
//                return String.format("%s %s", day, time);
//            }
//        }
//
//        /** When the Place opens. */
//        public Period.OpenClose open;
//
//        /** When the Place closes. */
//        public Period.OpenClose close;
//
//        @Override
//        public String toString() {
//            return String.format("%s - %s", open, close);
//        }
//    }

    /** Opening periods covering seven days, starting from Sunday, in chronological order. */
    @SerializedName("periods")
   public Period[] periods;
    public Period[] getPeriods() {
        return periods;
    }


    /**
     * The formatted opening hours for each day of the week, as an array of seven strings; for
     * example, {@code "Monday: 8:30 am – 5:30 pm"}.
     */
    @SerializedName("weekday_text")
    public String[] weekdayText;

    /**
     * Indicates that the place has permanently shut down.
     *
     * <p>Note: this field will be null if it isn't present in the response.
     */
    public Boolean permanentlyClosed;
    public Boolean getPermanentlyClosed() {
        return permanentlyClosed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[OpeningHours:");
        if (permanentlyClosed != null && permanentlyClosed) {
            sb.append(" permanentlyClosed");
        }
        if (openNow != null && openNow) {
            sb.append(" openNow");
        }
        sb.append(" ").append(Arrays.toString(periods));
        Log.d("Hours", sb.toString());
        return sb.toString();
    }
}

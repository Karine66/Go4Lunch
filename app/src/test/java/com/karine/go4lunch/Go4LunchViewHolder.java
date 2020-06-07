package com.karine.go4lunch;

import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Go4LunchViewHolder {

    private String name;
    private String vicinity;
    private String placeId;
    private String openUntil;
    private String closed;
    private String closingSoon;
    private PlaceDetailsResult result;

    @Before

    public void placeDetailsResult() {

        name = "Pizza Loupetis C.C";
        vicinity = "1, impasse de la distillerie, Toulouges";
        placeId = "ChIJJ845anZysBIRjaFS7pl4AW8";
        openUntil = "22h00";
        closed ="Closed";
        closingSoon = "Closing Soon";

//        result = new PlaceDetailsResult(name, vicinity, placeId);


    }

    @Test
    public void getHoursInfoTest() {




        assertEquals("Pizza Loupetis C.C",result.getName());
        assertEquals("1, impasse de la distillerie, Toulouges", result.getVicinity());
        assertEquals( "ChIJJ845anZysBIRjaFS7pl4AW8", result.getPlaceId());
        assertEquals("22h00", result.getOpeningHours());






    }
}

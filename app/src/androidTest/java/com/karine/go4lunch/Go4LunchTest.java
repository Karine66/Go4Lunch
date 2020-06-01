package com.karine.go4lunch;

import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.utils.Go4LunchStream;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;

public class Go4LunchTest {
    @Test
    public void fetchDetailsTest() throws Exception {
        Observable<PlaceDetail> observablePlaceDetail = Go4LunchStream.streamFetchDetails("ChIJJ845anZysBIRjaFS7pl4AW8");
        TestObserver<PlaceDetail> testObserver = new TestObserver<>();

        observablePlaceDetail.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        PlaceDetail placeDetail = testObserver.values().get(0);
        assertEquals("Pizza Loupetis C.C", placeDetail.getResult().getName());
    }
}

package shopmanager.model;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class LocationTest {


    @Test
    public void testGetLatitude() {
        Location location = new Location(1.1234, 5.6789);
        assertEquals(1.1234, location.getLatitude());
        assertEquals(5.6789, location.getLongitude());
    }

    @Test
    public void testDistanceBetweenLocations() {
        Location location1 = new Location(51.5384251, -0.10327830);
        Location location2 = new Location(51.51599890, -0.0669957);

        double distance = location1.distanceTo(location2);

        //distance according to Google maps is ~2.2 miles
        assertTrue(distance > 2.1 && distance < 2.3);
    }

}

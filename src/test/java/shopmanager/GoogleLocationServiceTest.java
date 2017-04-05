package shopmanager;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import shopmanager.model.Location;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GeocodingApi.class, GeocodingApiRequest.class})
public class GoogleLocationServiceTest {


    @Mock
    private GeoApiContext mockContext;

    @InjectMocks
    private GoogleLocationService locationService;

    @Test
    public void testLatLongIsReturnedForPostcode() throws Exception {
        String postCode = "N1 1AA";
        LatLng latLng = new LatLng(1.1, 2.2);
        GeocodingResult geocodingResult = new GeocodingResult();
        Geometry geometry = new Geometry();
        geometry.location = latLng;
        geocodingResult.geometry = geometry;
        GeocodingResult[] geocodingResults = new GeocodingResult[] { geocodingResult };

        GeocodingApiRequest geocodingApiRequest = mock(GeocodingApiRequest.class);
        when(geocodingApiRequest.await()).thenReturn(geocodingResults);

        mockStatic(GeocodingApi.class);

        when(GeocodingApi.geocode(mockContext, postCode))
                .thenReturn(geocodingApiRequest);

        Location location = locationService.findLocation(postCode);

        Location expected = new Location(1.1, 2.2);
        assertEquals(expected, location);
    }
}

package shopmanager;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GeocodingApi.class)
public class GoogleLocationServiceTest {



    @Test
    public void testLatLongReturnedForPostCode() {
        mockStatic(GeocodingApi.class);

//        when(GeocodingApi.newRequest())
    }
}

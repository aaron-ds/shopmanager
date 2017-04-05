package shopmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import shopmanager.model.Location;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Component
public class GoogleLocationService implements LocationService {

    private static final Logger log = LoggerFactory.getLogger(GoogleLocationService.class);

    @Value("${google.apikey}")
    private String apiKey;
    GeoApiContext context;


    @PostConstruct
    public void init() {
        context = new GeoApiContext().setApiKey(apiKey);
    }

    @Override
    public Location findLocation(String postCode) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, postCode).await();
            if (results.length > 0) {
                log.info("Received lat/lng <{}> for postCode <{}>", results[0].geometry.location, postCode);
                return new Location(results[0].geometry.location.lat, results[0].geometry.location.lng);
            }
        } catch (ApiException | InterruptedException | IOException e) {
            log.error("Exception caught when requesting coordinates for <{}>", postCode, e);
        }
        return null;
    }

}

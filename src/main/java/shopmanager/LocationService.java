package shopmanager;

import shopmanager.model.Location;

import java.util.Collection;

public interface LocationService {

    /**
     * Finds the lat/long of the given postcode
     * @param postCode the postcode
     * @return Location of the postcode
     */
    Location findLocation(String postCode);
}

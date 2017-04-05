package shopmanager;

import org.springframework.stereotype.Component;
import shopmanager.model.Location;

//@Component
public class GoogleLocationServiceTestDouble implements LocationService {

    public GoogleLocationServiceTestDouble() {
        System.out.println("creating test double");
    }

    @Override
    public Location findLocation(String postCode) {
        return new Location(5.1234, 8.4321);
    }
}

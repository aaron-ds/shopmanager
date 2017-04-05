package shopmanager;

import shopmanager.model.Location;
import shopmanager.model.Shop;

public interface ShopManager {

    /**
     * Adds a shop. If a shop already exists
     * @param shop The shop to add.
     * @return The existing shop that was replaced, or null if no shop was replaced
     */
    Shop addShop(Shop shop);

    /**
     * Finds the closest shop to the given location.
     * @param location location
     * @return The Shop that is closest to the given location.
     */
    Shop findClosestShop(Location location);
}

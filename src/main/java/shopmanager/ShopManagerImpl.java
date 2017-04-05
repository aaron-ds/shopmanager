package shopmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shopmanager.model.Location;
import shopmanager.model.Shop;
import shopmanager.model.Shop.Address;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

@Component
public class ShopManagerImpl implements ShopManager {

    private ConcurrentMap<String, Shop> shopStore = new ConcurrentHashMap();
    private LocationService locationService;
    private ExecutorService executorService;


    @Autowired
    public ShopManagerImpl(LocationService locationService, ExecutorService executorService) {
        this.locationService = locationService;
        this.executorService = executorService;
    }

    @Override
    public Shop addShop(Shop shop) {
        Shop existingShop = shopStore.put(shop.getShopName(), shop);
        getAndUpdateLocation(shop);
        return existingShop;
    }

    @Override
    public Shop findClosestShop(Location location) {
        Shop closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (Shop shop : shopStore.values()) {
            if (shop.getShopAddress().getLocation() != null) {
                double distance = location.distanceTo(shop.getShopAddress().getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closest = shop;
                }
            }
        }
        return closest;
    }

    protected void getAndUpdateLocation(Shop shop) {
        executorService.execute( () -> {
            Location location = locationService.findLocation(shop.getShopAddress().getPostCode());
            if (location != null) {
                Address address = shop.getShopAddress();
//                address.setLocation(location);
                Address newAddress = new Address(address.getNumber(), address.getPostCode());
                newAddress.setLocation(location);
                Shop shopWithLocation = new Shop(shop.getShopName(), newAddress);
                shopStore.replace(shop.getShopName(), shop, shopWithLocation);
            }
        });
    }

    protected Shop getShop(String shopName) {
        return shopStore.get(shopName);
    }

    protected Shop putShop(Shop shop) {
        return shopStore.put(shop.getShopName(), shop);
    }

}

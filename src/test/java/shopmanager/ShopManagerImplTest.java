package shopmanager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import concurrent.InstantExecutorService;
import shopmanager.LocationService;
import shopmanager.ShopManagerImpl;
import shopmanager.model.Location;
import shopmanager.model.Shop;
import shopmanager.model.Shop.Address;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShopManagerImplTest {

    @Mock LocationService mockLocationService;

    volatile Shop userAResponse;
    volatile Shop userBResponse;

    ShopManagerImpl shopManager;

    @Before
    public void setup() {
        shopManager = new ShopManagerImpl(mockLocationService, new InstantExecutorService());
    }

    @Test
    public void testShopIsAdded() {
        shopManager.addShop(new Shop("My Shop", new Address(5, "W1 1AA")));

        Shop shop = shopManager.getShop("My Shop");
        assertEquals("My Shop", shop.getShopName());
        assertEquals(5, shop.getShopAddress().getNumber());
        assertEquals("W1 1AA", shop.getShopAddress().getPostCode());
    }

    @Test
    public void testLocationIsUpdated() {
        Location l = new Location(1.1, 2.2);
        when(mockLocationService.findLocation("W1 1AA")).thenReturn(l);

        shopManager.addShop(new Shop("My Shop", new Address(5, "W1 1AA")));

        Shop shop = shopManager.getShop("My Shop");

        assertEquals("My Shop", shop.getShopName());
        assertEquals(5, shop.getShopAddress().getNumber());
        assertEquals("W1 1AA", shop.getShopAddress().getPostCode());
        assertEquals(l, shop.getShopAddress().getLocation());
    }

    @Test
    public void testLocationIsNotUpdatedIfShopHasChanged() {
        Location l = new Location(1.1, 2.2);
        Location l2 = new Location(5.5, 9.9);
        when(mockLocationService.findLocation("W1 1AA")).thenReturn(l);
        when(mockLocationService.findLocation("E1 1EE")).thenReturn(l2);

        Shop shop = new Shop("My Shop", new Address(5, "W1 1AA"));
        Shop updatedShop = new Shop("My Shop", new Address(5, "E1 1EE"));

        shopManager.putShop(shop);
        shopManager.putShop(updatedShop);
        shopManager.getAndUpdateLocation(shop);

        assertNull(shopManager.getShop("My Shop").getShopAddress().getLocation());

        shopManager.getAndUpdateLocation(updatedShop);

        assertEquals("My Shop", shopManager.getShop("My Shop").getShopName());
        assertEquals(l2, shopManager.getShop("My Shop").getShopAddress().getLocation());
    }

    @Test
    public void testLocationIsNotUpdatedIfLocationServiceLookupFails() {
        when(mockLocationService.findLocation("W1 1AA")).thenReturn(null);

        Shop shop = new Shop("My Shop", new Address(5, "W1 1AA"));

        shopManager.addShop(shop);

        assertEquals("My Shop", shopManager.getShop("My Shop").getShopName());
        assertNull(shopManager.getShop("My Shop").getShopAddress().getLocation());
    }

    @Test
    public void testIfAShopIsAddedAtTheSameTimeByTwoUsersOnlyOneReceivesAnUpdate() throws Exception {
        CountDownLatch start = new CountDownLatch(1);
        Address shopAddressA = new Address(1, "N1 1NN");
        Address shopAddressB = new Address(2, "W1 1AA");
        Shop shopA = new Shop("My Shop", shopAddressA);
        Shop shopB = new Shop("My Shop", shopAddressB);

        new Thread(() -> {
            try {
                start.await();
                userAResponse = shopManager.addShop(shopA);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }).start();

        new Thread(() -> {
            try {
                start.await();
                userBResponse = shopManager.addShop(shopB);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }).start();

        start.countDown();

        Thread.sleep(100);

        if (userAResponse != null) {
            assertNull(userBResponse);
            assertEquals(shopAddressB, userAResponse.getShopAddress());
        } else if (userBResponse != null) {
            assertNull(userAResponse);
            assertEquals(shopAddressA, userBResponse.getShopAddress());
        } else {
            throw new AssertionError();
        }
    }

    @Test
    public void testClosestShopIsReturned() {
        shopManager.putShop(new Shop("A shop", new Shop.Address(1, "N1 1NN", new Location(1.1, 2.2))));
        shopManager.putShop(new Shop("Another shop", new Shop.Address(2, "E1 1NN", new Location(10.1, 2.2))));
        shopManager.putShop(new Shop("One more shop", new Shop.Address(3, "W1 1NN", new Location(30.1, 2.2))));

        Shop closest;

        closest = shopManager.findClosestShop(new Location(1.2, 2.2));
        assertEquals("A shop", closest.getShopName());

        closest = shopManager.findClosestShop(new Location(9.9, 2.2));
        assertEquals("Another shop", closest.getShopName());

        closest = shopManager.findClosestShop(new Location(31.2, 2.2));
        assertEquals("One more shop", closest.getShopName());
    }

}

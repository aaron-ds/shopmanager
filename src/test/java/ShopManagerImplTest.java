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

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void testUpdate() {
        when(mockLocationService.findLocation("W1 1AA")).thenReturn(new Location(1.1, 2.2));

        Shop shop = new Shop("My Shop", new Address(5, "W1 1AA"));
        shopManager.addShop(shop);

        assertEquals("My Shop", shopManager.getShop("My Shop").getShopName());
    }

    @Test
    public void testLocationUpdate() {
        Location l = new Location(1.1, 2.2);
        when(mockLocationService.findLocation("W1 1AA")).thenReturn(l);

        Shop shop = new Shop("My Shop", new Address(5, "W1 1AA"));

        shopManager.addShop(shop);

        assertEquals("My Shop", shopManager.getShop("My Shop").getShopName());
        assertEquals(l, shopManager.getShop("My Shop").getShopAddress().getLocation());
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

}

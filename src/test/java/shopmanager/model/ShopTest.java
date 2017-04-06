package shopmanager.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShopTest {

    @Test
    public void testGetters() {
        Shop.Address address = new Shop.Address(1, "A1 1AA");
        Shop shop = new Shop("My Shop", address);

        assertEquals("My Shop", shop.getShopName());
        assertEquals(1, shop.getShopAddress().getNumber());
        assertEquals("A1 1AA", shop.getShopAddress().getPostCode());
    }
}

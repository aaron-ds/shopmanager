package shopmanager.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import shopmanager.*;
import shopmanager.model.Location;
import shopmanager.model.Shop;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(MockitoJUnitRunner.class)
public class ShopManagerControllerTest {


    @Mock
    private ShopManager shopManager;

    @InjectMocks
    private ShopManagerController controller;


    @Test
    public void testWhenNewShopIsAddedToManagerNothingIsReturned() {
        Shop shop = new Shop("My Shop", new Shop.Address(1, "N1 1NN"));

        Shop replaced = controller.handleCreateShop(shop);

        verify(shopManager).addShop(shop);
        assertNull(replaced);
    }

    @Test
    public void testWhenShopIsReplacedOldShopIsReturned() {
        Shop shop = new Shop("My Shop", new Shop.Address(1, "N1 1NN"));
        Shop existingShop = new Shop("My Shop", new Shop.Address(1, "E1 1EE"));

        when(shopManager.addShop(shop)).thenReturn(existingShop);

        Shop replaced = controller.handleCreateShop(shop);

        verify(shopManager).addShop(shop);
        assertEquals(replaced, existingShop);
    }

    @Test(expected = Exception.class)
    public void testExceptionIsThrownWhenShopToBeAddedIsNull() {
        controller.handleCreateShop(null);
    }

    @Test
    public void testClosestShopIsReturnedForGivenGeolocation() {
        Shop shop = new Shop("My Shop", new Shop.Address(1, "N1 1NN"));
        when(shopManager.findClosestShop(new Location(1.1, 2.2))).thenReturn(shop);

        Shop nearestShop = controller.handleFindNearestShop("1.1", "2.2");

        assertEquals(shop, nearestShop);
    }

    @Test
    public void testNoShopIsReturnedIfNearestShopCouldntBeFound() {
        Shop nearestShop = controller.handleFindNearestShop("1.1", "2.2");

        assertNull(nearestShop);
    }

    @Test(expected = Exception.class)
    public void testExceptionIsThrownIfLatitdeIsMissing() {
        controller.handleFindNearestShop(null, "2.2");
    }

    @Test(expected = Exception.class)
    public void testExceptionIsThrownIfLongitudeIsMissing() {
        controller.handleFindNearestShop("1.1", null);
    }

}

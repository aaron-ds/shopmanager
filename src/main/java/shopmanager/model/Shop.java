package shopmanager.model;

public class Shop {

    private String shopName;
    private Address shopAddress;

    public Shop() {}

    public Shop(String shopName, Address shopAddress) {
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Address getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(Address shopAddress) {
        this.shopAddress = shopAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shop)) {
            return false;
        }

        Shop shop = (Shop) o;

        if (shopName != null ? !shopName.equals(shop.shopName) : shop.shopName != null) {
            return false;
        }
        return shopAddress != null ? shopAddress.equals(shop.shopAddress) : shop.shopAddress == null;

    }

    @Override
    public int hashCode() {
        int result = shopName != null ? shopName.hashCode() : 0;
        result = 31 * result + (shopAddress != null ? shopAddress.hashCode() : 0);
        return result;
    }

    public static class Address {

        private int number;
        private String postCode;
        private Location location;

        public Address() {}

        public Address(int number, String postCode) {
            this.number = number;
            this.postCode = postCode;
        }

        public Address(int number, String postCode, Location location) {
            this.number = number;
            this.postCode = postCode;
            this.location = location;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Address)) {
                return false;
            }

            Address address = (Address) o;

            if (number != address.number) {
                return false;
            }
            if (postCode != null ? !postCode.equals(address.postCode) : address.postCode != null) {
                return false;
            }
            return location != null ? location.equals(address.location) : address.location == null;

        }

        @Override
        public int hashCode() {
            int result = number;
            result = 31 * result + (postCode != null ? postCode.hashCode() : 0);
            result = 31 * result + (location != null ? location.hashCode() : 0);
            return result;
        }
    }

}

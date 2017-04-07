# MVP for Asset Management Digital Challenge

Instructions

Add a Google geocoding enabled API key to resources/application.properties

Build using > mvn clean install

Test using  > mvn clean test

After building, the application can be run by navigating to the target directory and running > java -jar shop-manager-1.0-SNAPSHOT.jar

The service can be accessed at the following URL http://localhost:8080/shop

To add a shop, POST a json object e.g {"shopName": "my shop", "shopAddress": {"number": 4, "postCode": "N1 1EH"}} making sure the headers
are set to Content-Type: application/json

To retrieve the closest shop, make a GET request with the following query parameters ?lat=<value>&long=<value> replacing <value>
with the latitude and longitue values.

# MVC for Asset Management Digital Challenge

Instructions

add Google geocoding enabled API key to resources/application.properties

build using > mvn clean install
test using  > mvn clean test

after building, the application can be run by navigating to the target directory and running > java -jar shop-manager-1.0-SNAPSHOT.jar

The service can be accessed at the following URL http://localhost:8080/shop

to add a shop POST a json object e.g {"shopName": "my shop", "shopAddress": {"number": 4, "postCode": "N1 1EH"}} making sure the headers
are set to Content-Type: application/json

to retrieve the closest shop, make a GET request with the following query parameters ?lat=<value>&long=<value> replacing <value>
with the latitude and longitue values.

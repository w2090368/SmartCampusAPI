# SmartCampusAPI



I created this project which implements RESTful API using JAX-RS and an embedded Grizzly server. This project is for a university which manages their many rooms, sensors and sensor readings.

As required the data is stored in memory via thread safe data structures (ConcurrentHashMap).

****This API allows the university to:****

Create rooms and manage them
Register sensors and manage them
Retrieve sensor history or store it


****Technology used:****

Java
Maven
Grizzly	HTTP server
JAX-RS
postman(testing)

****API endpoints:****

GET /api/v1/rooms - get all rooms
POST /api/v1/rooms - create a room
GET /api/v1/rooms{id} - get a specific room
DELETE /api/v1/rooms{id} - delete a room


****Sensors:****

GET /api/v1/sensors - get all sensors
GET /api/v1/sensors?type=C02 - filter sensor by type
POST /api/v1/sensors - create a sensor

****Sensor readings:****

GET /api/v1/sensors/{id}/readings - get sensor readings
POST /api/v1/sensors/{id}/readings - add a new reading

****How to run the project:****

Github repository (to be cloned):
 	https://github.com/YOUR_USERNAME/SmartCampusAPI.git 

Open the project in Netbeans

Build the project then run it from main.java
To access the API enter the following in browser:
   	 http://localhost:8080/api/v1

****Curl commands:****

Get all rooms:
curl -X GET http://localhost:8080/api/v1/rooms 

Create a room:
curl -X POST http://localhost:8080/api/v1/rooms \ -H "Content-Type: application/json" \ -d '{"id":"LIB-301","name":"Library","capacity":50}' 

Create a sensor:
curl -X POST http://localhost:8080/api/v1/sensors \ -H "Content-Type: application/json" \ -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":0,"roomId":"LIB-301"}' 

Get sensor (filtered):
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature" 

Add a sensor reading:
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \ -H "Content-Type: application/json" \ -d '{"value":22.5}' 

**Report answers:**

1.1:
Regarding JAX-RS the default lifecycle of a resource class is per request. This means that the JAX-RS runtime makes a new instance of the resource class in order to handle every incoming HTTP request. Once the request is completed the instance is available for garbage collection (no longer needed). This is advantageous as instances of the resource class are not shared between threads making it thread safe. Data structures such as ArrayList or HashMap which are shared in memory are accessed by multiple requests simultaneously. This can lead to inconsistent data. To prevent this synchronizing techniques or ConcurrentHashMaps can be applied to ensure consistent data access. These are examples of thread safe practices.


1.2:
The use of HATEOAS is a major component of advanced RESTful design as it allows dynamic links to related resources within their responses. This makes navigating possible for clients without depending on static documentation as the API is self descriptive. Due to this the client and server are loosely coupled allowing for improved developer experience as it is more maintainable and flexible. 


2.1:
Regarding returning a list of rooms, returning only the ID compared to returning the full room object has major advantages such as better network efficiency and the response size is smaller. When receiving the entire room object, the client will get all the information in a single response however this increases the payload size, despite making it easier for clients to use especially with larger amounts of data. However with larger datasets, clients will need to make extra requests in order to retrieve their desired information, which also increases client side processing. That being said, there is a trade off between performance and convenience. 


2.2:
The DELETE operation in my implementation is considered idempotent as the first DELETE request removes the room and returns a 204 No Content status code. If the same DELETE request is sent again the code will return a 404 not found status code as the room doesn’t exist. This shows that DELETE is idempotent as repeating the same request does not change the state of the server after the first deletion.

3.1:
The consequences if a client attempts to send data in a different format such as text/plain or application/xml is that the request will not be accepted and the server will throw a 415 Unsupported Media Type response. This indicates to the client that the format of the request body isn’t supported by the API. The reason being is @Consumes(MediaType.APPLICATION_JSON) restricts it so that the only acceptable input is JSON input.


3.2:
The query parameter approach is generally considered superior for filtering and searching as it allows for additional conditions to be added to the request. For example, /sensor?type=C02 shows that we are searching for sensors only with the “C02”. On top of that the client is able to add additional filters such as type=C02&status=ACTIVE. However, using /sensor/type/C02 makes it look like a fixed resource rather than a search condition. 


4.1:
The benefits of the Sub-Resource Locator pattern is that it moves nested logic into separate classes which helps to keep the code more manageable and easy to read or maintain. This improves the API design. This also reduces complexities as each resource can manage its own instead of putting everything into one large resource class. This is particularly beneficial in larger APIs as it helps manage the complexities better.

5.2:
HTTP 422 is considered more semantically accurate than 404 because the request is valid and the endpoint exists. However, the data held within the JSON is not correct which in this case the issue is that the roomId in the request doesn’t refer to a real room. A 422 shows that the server understands the request but cannot process the data correctly. While a 404 shows that the URL itself doesn’t exist. Therefore, a 422 is more semantically accurate compared to a 404.

5.4:
Exposing java stack traces to external API consumers poses a security risk as it shows sensitive information about the systems structure. This can include file paths and class names and package structures. This sensitive information, if exposed, can help hackers understand the system structure, how it's built and find possible weak points in order to exploit the system. To help prevent this, instead of revealing internal information that can help an attacker, the API should instead return a generic error message which would make it much more difficult for an attacker to gather information about the system.


5.5:
Using JAX-RS filters for cross-cutting concerns such as logging is advantageous over logger.info() as it makes the code more manageable and easier to maintain. The reason being is that unlike logger.info(), JAX-RS filters allow us to handle logging in one place, which also allows us to make sure all requests and responses are logged in an organized way. On the other hand logger.info() statements have to be placed inside all resource methods which isn’t as organised as using JAX-RS filters. The use of filters also avoids duplication as everything is one filter. This also allows the filter to handle its own logic whilst the resource classes can handle their prioritized logic, which helps with separation concerns.


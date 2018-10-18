# JSON-DIFF

## Indroduction

This is a sample spring boot application to:

 * Provide http endpoints that accepts and persists on a H2 database JSON base64 encoded binary data, grouped by a identifier. The elements can be set at a given position{LEFT/RIGHT} or be added to an unfixed stack.   
 * Provide an endpoint to compare all elements on a given identifier and provide insight on the differences.
 * Provide an endpoint to clear all elements on a given identifier. 
 * Allow multiple implementations for comparison according to profile configuration. 

### Assumptions

The following assumptions were used:
 
 * The provided value is a valid base64 json binary data.
 * The provided value won't represent a large data exceeding H2 server limits.
 * The provided identifier will be a valid path variable not exceeding a 255 character limit. 
 * Content difference insight will show only display the name of the root JSON nodes with a identified difference.


### Technology

The following technologies were used:

Main Technologies:

* Coded in Java.
* Maven dependency management.  

Framework/Endpoints:

* [Spring boot](https://spring.io/projects/spring-boot) framework.

JSON parsing:

* [Javax DatatypeConverter](https://docs.oracle.com/javase/7/docs/api/javax/xml/bind/DatatypeConverter.html) for base64 decoding.
* [Jackson](https://github.com/FasterXML/jackson) for JSON parsing.
* [Guava](https://github.com/google/guava/wiki/CollectionUtilitiesExplained#maps) for JSON comparison.

Persistence:

* H2 in memory database.
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa).

Testing:

* [JUnit](https://junit.org/junit4/) for test execution.
* [Spring test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) for test execution.
* [Apache Commons IO](https://commons.apache.org/proper/commons-io/) for reading JSON test files.

### Running

#### Requirements 

* Java 1.8 
* Maven

#### Setting up

This app can be run with the following commands at the project's root folder:

with maven: ``mvn spring-boot:run``

with java:

* building: ``mvn clean -U package``
* running: ``java -jar ./target/json-diff-1.0.0.jar``

#### Possible profile behaviors

* content: The default profile. The comparison will run by size and if no size difference is found, content comparison will run. 
* size: Only size difference will run. Content difference on equal sized data will be ignored.


### Usage Guide

* POST /v1/diff/{id}/left: Add or replace the left element to be compared under the provided identifier. 
 	- Params: 
 		{id}: Comparison identifier
 		Request Body: JSON base64 encoded binary data.
 		
* POST /v1/diff/{id}/right: Add or replace the right element to be compared under the provided identifier. 
 	- Params: 
 		{id}: Comparison identifier
 		Request Body: JSON base64 encoded binary data.
 		
* POST /v1/diff/{id}/add: Add a new element to be compared at unfixed position. 
 	- Params: 
 		{id}: Comparison identifier
 		Request Body: JSON base64 encoded binary data.
 		
* GET /v1/diff/{id}: Diff the existing elements under the provided identifier. 
 	- Params: 
 		{id}: Comparison identifier
 		
* GET /v1/diff/{id}/view: List the existing elements under the provided identifier. 
 	- Params: 
 		{id}: Comparison identifier
 		
* DELETE /v1/diff/{id}: Delete a comparison and its existing elements under the provided identifier. 
 	- Params: 
 		{id}: Comparison identifier
 		
### Future

 * Authentication and comparisons separated per user
 * Base64 conformance validation
 * JSON validation and error insight on invalid JSONs.
 * Change database persistence to support big data.
 * New profiles and Compare Components to compare different types of data. 
  

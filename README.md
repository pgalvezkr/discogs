# Discogs API Consumer and Local Database API

This project consumes the Discogs API to retrieve artist data and stores it in a PostgreSQL database. It then exposes its own REST API to interact with this stored data, allowing you to search for artists, retrieve discographies, and compare artists based on various criteria.

## Steps to Run the Application

### 1. Prerequisites
- **Java 17** (LTS)
- **Spring Boot** (latest stable version)
- **Maven** (latest stable version)

### 2. Database Setup
Inside the project, you will find a directory named `database`, which contains the SQL script required to create the necessary tables in the PostgreSQL database.

- Navigate to the `database` directory.
- Execute the SQL script to create the required tables.

### 3. Database Connection Configuration
Before running the application, configure the database connection by updating the following properties.

- Open the `application.properties` (or `application.yml`) file located in the `src/main/resources` directory.
- Modify the database connection settings to match your PostgreSQL instance.

Example configuration in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=org.postgresql.Driver
```

### 4. Running the Application
Once the database connection is configured, you can run the application by executing the DiscogApplication class.
The application will be accessible at http://localhost:8080.

## 5. API Documentation
#### 1. Search Artist
This endpoint allows get information about a specific artist.

   - Endpoint: /api/artists/search-artist
   - Method: GET
   - Path Parameter: name (the name of the artist to search for)
   - Request: Example URL: http://localhost:8080/api/artists/search-artist?name=avicci
   - Response:
```properties
[
  {
      "id": 1235958,
      "name": "Avicii",
      "discogId": 1235958
  },
  {
      "id": 85239,
      "name": "Greg Fitzgerald",
      "discogId": 85239
  }
]
```
#### 2. Get Discography
This endpoint allows get discography of a specific artist.
- Endpoint: /api/release/generate
- Method: POST
- Path Parameter: name (the name of the artist to search for)
- Request Body:
```properties
{
  "id": 1235958,   // Artist ID obtained from the previous endpoint
  "name": "Avicii", // Artist name obtained from the previous endpoint
  "discogId": 1235958 // Discogs ID obtained from the previous endpoint
}
```
- Response:
```properties
The discography has been saved and the id for the artist is 1
```
#### 3. Comparison Artist
This endpoint allows comparing artists based on different criteria such as TAG, RELEASES, ACTIVE_YEARS, and GENRE. If no criteria are provided, all available criteria are considered.
- Endpoint: /api/artists/comparison
- Method: POST
- Path Parameter: name (the name of the artist to search for)
- Request Body:
```properties
{
"artistIds": [1, 3],   // Artist IDs obtained from the previous endpoint
"criteriaTypes": ["TAG", "RELEASES", "ACTIVE_YEARS", "GENRE"] // One or more criteria types
}
```
- Response:
```properties
{
  "artistResponseDtos": [
    {
      "idArtist": 1,
      "countReleases": 50,
      "activeYears": "10",
      "mostCommonTag": "Avicii Music AB",
      "mostCommonGenre": "Electronic"
    },
    {
      "idArtist": 3,
      "countReleases": 50,
      "activeYears": "49",
      "mostCommonTag": "EMI",
      "mostCommonGenre": "Rock"
    }
  ]
}
```
You can use Postman to test the APIs by sending requests to the provided URLs with the appropriate parameters or request bodies.

package ru.fa.carrental.client.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.fa.carrental.client.model.Car;
import ru.fa.carrental.client.model.CarCategory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

/**
 * Simple REST client for server API.
 * <p>
 * Uses Java 11 HttpClient to communicate with the Spring Boot backend.
 * </p>
 */
public class CarApi {
    private final String baseUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * @param baseUrl base URL of the server API (e.g. http://localhost:8080/api/cars)
     */
    public CarApi(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Retrieves the list of cars from the server.
     *
     * @param query      optional query string for filtering by model
     * @param categoryId optional category id
     * @return list of cars, or {@code null} if an error occurred
     */
    public List<Car> listCars(String query, Long categoryId) {
        try {
            StringBuilder url = new StringBuilder(baseUrl);
            String separator = "?";
            if (query != null && !query.isBlank()) {
                url.append(separator).append("q=").append(query.replace(" ", "%20"));
                separator = "&";
            }
            if (categoryId != null) {
                url.append(separator).append("categoryId=").append(categoryId);
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url.toString()))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Car> listCars(String query) {
        return listCars(query, null);
    }

    /**
     * Adds a new car on the server side.
     *
     * @param car car to persist
     * @return persisted car with generated ID or {@code null} if failed
     */
    public Car createCar(Car car) {
        try {
            String body = mapper.writeValueAsString(car);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), Car.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates an existing car.
     *
     * @param car car with updated fields
     * @return updated car or {@code null} if the update failed
     */
    public Car updateCar(Car car) {
        try {
            String body = mapper.writeValueAsString(car);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/" + car.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), Car.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Removes a car from the server.
     *
     * @param id identifier of the car
     */
    public void deleteCar(long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/" + id))
                    .DELETE()
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves aggregated statistics for the car catalog.
     *
     * @return statistics record or {@code null} if the request failed
     */
    public Stats getStats() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/stats"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), Stats.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lists all categories registered on the server.
     */
    public List<CarCategory> listCategories() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl.replace("/cars", "/categories")))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public record Stats(int count, double avgPrice, double minPrice, double maxPrice) {
    }
}

package com.petclinic.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

abstract class AbstractIT {

    static final String BASE = "http://localhost:8080/petclinic/api";
    static final HttpClient HTTP = HttpClient.newHttpClient();
    static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    static HttpResponse<String> get(String path) throws Exception {
        return HTTP.send(
                HttpRequest.newBuilder(URI.create(BASE + path)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }

    static HttpResponse<String> post(String path, Object body) throws Exception {
        return HTTP.send(
                HttpRequest.newBuilder(URI.create(BASE + path))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(body)))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }

    static HttpResponse<String> put(String path, Object body) throws Exception {
        return HTTP.send(
                HttpRequest.newBuilder(URI.create(BASE + path))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(body)))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }

    static HttpResponse<String> delete(String path) throws Exception {
        return HTTP.send(
                HttpRequest.newBuilder(URI.create(BASE + path)).DELETE().build(),
                HttpResponse.BodyHandlers.ofString());
    }

    static JsonNode json(HttpResponse<String> resp) throws Exception {
        return MAPPER.readTree(resp.body());
    }

    static long id(HttpResponse<String> resp) throws Exception {
        return json(resp).get("id").asLong();
    }
}

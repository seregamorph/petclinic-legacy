package com.petclinic.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;

import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OwnersIT extends AbstractIT {

    private long ownerId;

    @Test @Order(1)
    void createOwner_returns201WithId() throws Exception {
        HttpResponse<String> resp = post("/owners", Map.of(
                "firstName", "John",
                "lastName",  "Doe",
                "address",   "123 Main St",
                "city",      "Springfield",
                "telephone", "5551234567"
        ));
        assertEquals(201, resp.statusCode());
        JsonNode body = json(resp);
        assertTrue(body.has("id"));
        ownerId = body.get("id").asLong();
        assertEquals("John", body.get("firstName").asText());
        assertEquals("Doe",  body.get("lastName").asText());
    }

    @Test @Order(2)
    void getOwner_returns200WithCorrectData() throws Exception {
        HttpResponse<String> resp = get("/owners/" + ownerId);
        assertEquals(200, resp.statusCode());
        JsonNode body = json(resp);
        assertEquals(ownerId,      body.get("id").asLong());
        assertEquals("John",       body.get("firstName").asText());
        assertEquals("Doe",        body.get("lastName").asText());
        assertEquals("Springfield",body.get("city").asText());
    }

    @Test @Order(3)
    void listOwners_returns200WithArray() throws Exception {
        HttpResponse<String> resp = get("/owners");
        assertEquals(200, resp.statusCode());
        assertTrue(json(resp).isArray());
    }

    @Test @Order(4)
    void updateOwner_returns200WithUpdatedData() throws Exception {
        HttpResponse<String> resp = put("/owners/" + ownerId, Map.of(
                "firstName", "Jane",
                "lastName",  "Doe",
                "address",   "456 Elm St",
                "city",      "Shelbyville",
                "telephone", "5559876543"
        ));
        assertEquals(200, resp.statusCode());
        JsonNode body = json(resp);
        assertEquals("Jane",        body.get("firstName").asText());
        assertEquals("Shelbyville", body.get("city").asText());
    }

    @Test @Order(5)
    void deleteOwner_returns204() throws Exception {
        HttpResponse<String> resp = delete("/owners/" + ownerId);
        assertEquals(204, resp.statusCode());
    }

    @Test @Order(6)
    void getDeletedOwner_returns404() throws Exception {
        HttpResponse<String> resp = get("/owners/" + ownerId);
        assertEquals(404, resp.statusCode());
    }

    @Test @Order(7)
    void updateNonExistentOwner_returns404() throws Exception {
        HttpResponse<String> resp = put("/owners/" + ownerId, Map.of(
                "firstName", "Ghost",
                "lastName",  "Owner",
                "address",   "Nowhere",
                "city",      "NoCity",
                "telephone", "0000000000"
        ));
        assertEquals(404, resp.statusCode());
    }

    @Test @Order(8)
    void deleteNonExistentOwner_returns404() throws Exception {
        HttpResponse<String> resp = delete("/owners/" + ownerId);
        assertEquals(404, resp.statusCode());
    }
}

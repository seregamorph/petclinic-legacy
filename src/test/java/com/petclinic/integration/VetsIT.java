package com.petclinic.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;

import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VetsIT extends AbstractIT {

    private long vetId;

    @Test @Order(1)
    void createVet_returns201WithId() throws Exception {
        HttpResponse<String> resp = post("/vets", Map.of(
                "firstName", "Alice",
                "lastName",  "Smith",
                "specialty", "Surgery"
        ));
        assertEquals(201, resp.statusCode());
        JsonNode body = json(resp);
        assertTrue(body.has("id"));
        vetId = body.get("id").asLong();
        assertEquals("Alice",   body.get("firstName").asText());
        assertEquals("Surgery", body.get("specialty").asText());
    }

    @Test @Order(2)
    void getVet_returns200WithCorrectData() throws Exception {
        HttpResponse<String> resp = get("/vets/" + vetId);
        assertEquals(200, resp.statusCode());
        JsonNode body = json(resp);
        assertEquals(vetId,    body.get("id").asLong());
        assertEquals("Alice",  body.get("firstName").asText());
        assertEquals("Smith",  body.get("lastName").asText());
        assertEquals("Surgery",body.get("specialty").asText());
    }

    @Test @Order(3)
    void listVets_returns200WithArray() throws Exception {
        HttpResponse<String> resp = get("/vets");
        assertEquals(200, resp.statusCode());
        assertTrue(json(resp).isArray());
    }

    @Test @Order(4)
    void updateVet_returns200WithUpdatedData() throws Exception {
        HttpResponse<String> resp = put("/vets/" + vetId, Map.of(
                "firstName", "Alice",
                "lastName",  "Smith",
                "specialty", "Dentistry"
        ));
        assertEquals(200, resp.statusCode());
        assertEquals("Dentistry", json(resp).get("specialty").asText());
    }

    @Test @Order(5)
    void deleteVet_returns204() throws Exception {
        assertEquals(204, delete("/vets/" + vetId).statusCode());
    }

    @Test @Order(6)
    void getDeletedVet_returns404() throws Exception {
        assertEquals(404, get("/vets/" + vetId).statusCode());
    }

    @Test @Order(7)
    void updateNonExistentVet_returns404() throws Exception {
        HttpResponse<String> resp = put("/vets/" + vetId, Map.of(
                "firstName", "Ghost",
                "lastName",  "Vet",
                "specialty", "None"
        ));
        assertEquals(404, resp.statusCode());
    }

    @Test @Order(8)
    void deleteNonExistentVet_returns404() throws Exception {
        assertEquals(404, delete("/vets/" + vetId).statusCode());
    }
}

package com.petclinic.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;

import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetsIT extends AbstractIT {

    private long ownerId;
    private long petId;

    @BeforeAll
    void createOwner() throws Exception {
        HttpResponse<String> resp = post("/owners", Map.of(
                "firstName", "Pet",
                "lastName",  "Owner",
                "address",   "1 Pet Lane",
                "city",      "Petville",
                "telephone", "5550000001"
        ));
        assertEquals(201, resp.statusCode(), "prerequisite owner creation failed");
        ownerId = id(resp);
    }

    @AfterAll
    void deleteOwner() throws Exception {
        delete("/owners/" + ownerId);
    }

    @Test @Order(1)
    void createPet_returns201WithId() throws Exception {
        HttpResponse<String> resp = post("/pets", Map.of(
                "name",      "Buddy",
                "birthDate", "2020-05-10",
                "type",      "dog",
                "ownerId",   ownerId
        ));
        assertEquals(201, resp.statusCode());
        JsonNode body = json(resp);
        assertTrue(body.has("id"));
        petId = body.get("id").asLong();
        assertEquals("Buddy",  body.get("name").asText());
        assertEquals("dog",    body.get("type").asText());
        assertEquals(ownerId,  body.get("ownerId").asLong());
    }

    @Test @Order(2)
    void getPet_returns200WithCorrectData() throws Exception {
        HttpResponse<String> resp = get("/pets/" + petId);
        assertEquals(200, resp.statusCode());
        JsonNode body = json(resp);
        assertEquals(petId,   body.get("id").asLong());
        assertEquals("Buddy", body.get("name").asText());
        assertEquals(ownerId, body.get("ownerId").asLong());
    }

    @Test @Order(3)
    void listPets_returns200WithArray() throws Exception {
        HttpResponse<String> resp = get("/pets");
        assertEquals(200, resp.statusCode());
        assertTrue(json(resp).isArray());
    }

    @Test @Order(4)
    void listPetsByOwner_returns200ContainingCreatedPet() throws Exception {
        HttpResponse<String> resp = get("/pets?ownerId=" + ownerId);
        assertEquals(200, resp.statusCode());
        JsonNode arr = json(resp);
        assertTrue(arr.isArray());
        boolean found = false;
        for (JsonNode node : arr) {
            if (node.get("id").asLong() == petId) { found = true; break; }
        }
        assertTrue(found, "created pet should appear in owner-filtered list");
    }

    @Test @Order(5)
    void updatePet_returns200WithUpdatedData() throws Exception {
        HttpResponse<String> resp = put("/pets/" + petId, Map.of(
                "name",      "Max",
                "birthDate", "2020-05-10",
                "type",      "dog",
                "ownerId",   ownerId
        ));
        assertEquals(200, resp.statusCode());
        assertEquals("Max", json(resp).get("name").asText());
    }

    @Test @Order(6)
    void deletePet_returns204() throws Exception {
        assertEquals(204, delete("/pets/" + petId).statusCode());
    }

    @Test @Order(7)
    void getDeletedPet_returns404() throws Exception {
        assertEquals(404, get("/pets/" + petId).statusCode());
    }

    @Test @Order(8)
    void updateNonExistentPet_returns404() throws Exception {
        HttpResponse<String> resp = put("/pets/" + petId, Map.of(
                "name",      "Ghost",
                "birthDate", "2020-01-01",
                "type",      "cat",
                "ownerId",   ownerId
        ));
        assertEquals(404, resp.statusCode());
    }

    @Test @Order(9)
    void deleteNonExistentPet_returns404() throws Exception {
        assertEquals(404, delete("/pets/" + petId).statusCode());
    }
}

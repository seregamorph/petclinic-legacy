package com.petclinic.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;

import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VisitsIT extends AbstractIT {

    private long ownerId;
    private long petId;
    private long visitId;

    @BeforeAll
    void createOwnerAndPet() throws Exception {
        HttpResponse<String> ownerResp = post("/owners", Map.of(
                "firstName", "Visit",
                "lastName",  "Owner",
                "address",   "2 Visit Ave",
                "city",      "Visitville",
                "telephone", "5550000002"
        ));
        assertEquals(201, ownerResp.statusCode(), "prerequisite owner creation failed");
        ownerId = id(ownerResp);

        HttpResponse<String> petResp = post("/pets", Map.of(
                "name",      "Whiskers",
                "birthDate", "2019-03-15",
                "type",      "cat",
                "ownerId",   ownerId
        ));
        assertEquals(201, petResp.statusCode(), "prerequisite pet creation failed");
        petId = id(petResp);
    }

    @AfterAll
    void cleanup() throws Exception {
        delete("/pets/" + petId);
        delete("/owners/" + ownerId);
    }

    @Test @Order(1)
    void createVisit_returns201WithId() throws Exception {
        HttpResponse<String> resp = post("/visits", Map.of(
                "petId",       petId,
                "visitDate",   "2024-06-01",
                "description", "Annual checkup"
        ));
        assertEquals(201, resp.statusCode());
        JsonNode body = json(resp);
        assertTrue(body.has("id"));
        visitId = body.get("id").asLong();
        assertEquals(petId,            body.get("petId").asLong());
        assertEquals("Annual checkup", body.get("description").asText());
    }

    @Test @Order(2)
    void getVisit_returns200WithCorrectData() throws Exception {
        HttpResponse<String> resp = get("/visits/" + visitId);
        assertEquals(200, resp.statusCode());
        JsonNode body = json(resp);
        assertEquals(visitId,          body.get("id").asLong());
        assertEquals(petId,            body.get("petId").asLong());
        assertEquals("Annual checkup", body.get("description").asText());
    }

    @Test @Order(3)
    void listVisits_returns200WithArray() throws Exception {
        HttpResponse<String> resp = get("/visits");
        assertEquals(200, resp.statusCode());
        assertTrue(json(resp).isArray());
    }

    @Test @Order(4)
    void listVisitsByPet_returns200ContainingCreatedVisit() throws Exception {
        HttpResponse<String> resp = get("/visits?petId=" + petId);
        assertEquals(200, resp.statusCode());
        JsonNode arr = json(resp);
        assertTrue(arr.isArray());
        boolean found = false;
        for (JsonNode node : arr) {
            if (node.get("id").asLong() == visitId) { found = true; break; }
        }
        assertTrue(found, "created visit should appear in pet-filtered list");
    }

    @Test @Order(5)
    void updateVisit_returns200WithUpdatedData() throws Exception {
        HttpResponse<String> resp = put("/visits/" + visitId, Map.of(
                "petId",       petId,
                "visitDate",   "2024-06-01",
                "description", "Follow-up checkup"
        ));
        assertEquals(200, resp.statusCode());
        assertEquals("Follow-up checkup", json(resp).get("description").asText());
    }

    @Test @Order(6)
    void deleteVisit_returns204() throws Exception {
        assertEquals(204, delete("/visits/" + visitId).statusCode());
    }

    @Test @Order(7)
    void getDeletedVisit_returns404() throws Exception {
        assertEquals(404, get("/visits/" + visitId).statusCode());
    }

    @Test @Order(8)
    void updateNonExistentVisit_returns404() throws Exception {
        HttpResponse<String> resp = put("/visits/" + visitId, Map.of(
                "petId",       petId,
                "visitDate",   "2024-01-01",
                "description", "Ghost visit"
        ));
        assertEquals(404, resp.statusCode());
    }

    @Test @Order(9)
    void deleteNonExistentVisit_returns404() throws Exception {
        assertEquals(404, delete("/visits/" + visitId).statusCode());
    }
}

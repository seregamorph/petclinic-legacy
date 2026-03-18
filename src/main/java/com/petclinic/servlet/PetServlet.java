package com.petclinic.servlet;

import com.petclinic.model.Pet;
import com.petclinic.service.PetService;
import com.petclinic.util.JsonUtil;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Handles /api/pets and /api/pets/{id}
 * Optional query param: ?ownerId=N to filter by owner
 */
public class PetServlet extends HttpServlet {

    private final PetService petService = PetService.getInstance();
    private final JsonUtil json = JsonUtil.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        try {
            if (id == null) {
                String ownerIdParam = req.getParameter("ownerId");
                if (ownerIdParam != null) {
                    json.writeJson(resp, 200, petService.getPetsByOwner(Long.parseLong(ownerIdParam)));
                } else {
                    json.writeJson(resp, 200, petService.getAllPets());
                }
            } else {
                Optional<Pet> pet = petService.getPet(id);
                if (pet.isPresent()) json.writeJson(resp, 200, pet.get());
                else json.writeError(resp, 404, "Pet not found");
            }
        } catch (NumberFormatException e) {
            json.writeError(resp, 400, "Invalid ownerId parameter");
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Pet pet = json.readBody(req, Pet.class);
            json.writeJson(resp, 201, petService.createPet(pet));
        } catch (IllegalArgumentException e) {
            json.writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        if (id == null) { json.writeError(resp, 400, "Missing pet id"); return; }
        try {
            Pet incoming = json.readBody(req, Pet.class);
            Optional<Pet> updated = petService.updatePet(id, incoming);
            if (updated.isPresent()) json.writeJson(resp, 200, updated.get());
            else json.writeError(resp, 404, "Pet not found");
        } catch (IllegalArgumentException e) {
            json.writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        if (id == null) { json.writeError(resp, 400, "Missing pet id"); return; }
        try {
            if (petService.deletePet(id)) resp.setStatus(204);
            else json.writeError(resp, 404, "Pet not found");
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }
}

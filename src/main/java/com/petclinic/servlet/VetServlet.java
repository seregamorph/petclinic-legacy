package com.petclinic.servlet;

import com.petclinic.model.Vet;
import com.petclinic.service.VetService;
import com.petclinic.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Handles /api/vets and /api/vets/{id}
 */
@WebServlet(urlPatterns = {"/api/vets/*"})
public class VetServlet extends HttpServlet {

    private final VetService vetService = VetService.getInstance();
    private final JsonUtil json = JsonUtil.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        try {
            if (id == null) {
                json.writeJson(resp, 200, vetService.getAllVets());
            } else {
                Optional<Vet> vet = vetService.getVet(id);
                if (vet.isPresent()) json.writeJson(resp, 200, vet.get());
                else json.writeError(resp, 404, "Vet not found");
            }
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Vet vet = json.readBody(req, Vet.class);
            json.writeJson(resp, 201, vetService.createVet(vet));
        } catch (IllegalArgumentException e) {
            json.writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        if (id == null) { json.writeError(resp, 400, "Missing vet id"); return; }
        try {
            Vet incoming = json.readBody(req, Vet.class);
            Optional<Vet> updated = vetService.updateVet(id, incoming);
            if (updated.isPresent()) json.writeJson(resp, 200, updated.get());
            else json.writeError(resp, 404, "Vet not found");
        } catch (IllegalArgumentException e) {
            json.writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        if (id == null) { json.writeError(resp, 400, "Missing vet id"); return; }
        try {
            if (vetService.deleteVet(id)) resp.setStatus(204);
            else json.writeError(resp, 404, "Vet not found");
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }
}

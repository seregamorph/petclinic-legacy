package com.petclinic.servlet;

import com.petclinic.model.Owner;
import com.petclinic.service.OwnerService;
import com.petclinic.util.JsonUtil;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Handles /api/owners and /api/owners/{id}
 */
public class OwnerServlet extends HttpServlet {

    private final OwnerService ownerService = OwnerService.getInstance();
    private final JsonUtil json = JsonUtil.getInstance();

    // GET /api/owners        -> list all
    // GET /api/owners/{id}   -> get one
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        try {
            if (id == null) {
                json.writeJson(resp, 200, ownerService.getAllOwners());
            } else {
                Optional<Owner> owner = ownerService.getOwner(id);
                if (owner.isPresent()) json.writeJson(resp, 200, owner.get());
                else json.writeError(resp, 404, "Owner not found");
            }
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    // POST /api/owners -> create
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Owner owner = json.readBody(req, Owner.class);
            json.writeJson(resp, 201, ownerService.createOwner(owner));
        } catch (IllegalArgumentException e) {
            json.writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    // PUT /api/owners/{id} -> update
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        if (id == null) { json.writeError(resp, 400, "Missing owner id"); return; }
        try {
            Owner incoming = json.readBody(req, Owner.class);
            Optional<Owner> updated = ownerService.updateOwner(id, incoming);
            if (updated.isPresent()) json.writeJson(resp, 200, updated.get());
            else json.writeError(resp, 404, "Owner not found");
        } catch (IllegalArgumentException e) {
            json.writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    // DELETE /api/owners/{id} -> delete
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        if (id == null) { json.writeError(resp, 400, "Missing owner id"); return; }
        try {
            if (ownerService.deleteOwner(id)) resp.setStatus(204);
            else json.writeError(resp, 404, "Owner not found");
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }
}

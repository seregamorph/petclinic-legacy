package com.petclinic.servlet;

import com.petclinic.model.Visit;
import com.petclinic.service.VisitService;
import com.petclinic.util.JsonUtil;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Handles /api/visits and /api/visits/{id}
 * Optional query param: ?petId=N to filter by pet
 */
public class VisitServlet extends HttpServlet {

    private final VisitService visitService = VisitService.getInstance();
    private final JsonUtil json = JsonUtil.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        try {
            if (id == null) {
                String petIdParam = req.getParameter("petId");
                if (petIdParam != null) {
                    json.writeJson(resp, 200, visitService.getVisitsByPet(Long.parseLong(petIdParam)));
                } else {
                    json.writeJson(resp, 200, visitService.getAllVisits());
                }
            } else {
                Optional<Visit> visit = visitService.getVisit(id);
                if (visit.isPresent()) json.writeJson(resp, 200, visit.get());
                else json.writeError(resp, 404, "Visit not found");
            }
        } catch (NumberFormatException e) {
            json.writeError(resp, 400, "Invalid petId parameter");
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Visit visit = json.readBody(req, Visit.class);
            json.writeJson(resp, 201, visitService.createVisit(visit));
        } catch (IllegalArgumentException e) {
            json.writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        if (id == null) { json.writeError(resp, 400, "Missing visit id"); return; }
        try {
            Visit incoming = json.readBody(req, Visit.class);
            Optional<Visit> updated = visitService.updateVisit(id, incoming);
            if (updated.isPresent()) json.writeJson(resp, 200, updated.get());
            else json.writeError(resp, 404, "Visit not found");
        } catch (IllegalArgumentException e) {
            json.writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = JsonUtil.parseId(req.getPathInfo());
        if (id == null) { json.writeError(resp, 400, "Missing visit id"); return; }
        try {
            if (visitService.deleteVisit(id)) resp.setStatus(204);
            else json.writeError(resp, 404, "Visit not found");
        } catch (Exception e) {
            json.writeError(resp, 500, e.getMessage());
        }
    }
}

package com.petclinic.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Singleton wrapper around Jackson ObjectMapper.
 */
public class JsonUtil {

    private static JsonUtil instance;

    private final ObjectMapper mapper;

    private JsonUtil() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static synchronized JsonUtil getInstance() {
        if (instance == null) {
            instance = new JsonUtil();
        }
        return instance;
    }

    public <T> T readBody(HttpServletRequest req, Class<T> type) throws IOException {
        return mapper.readValue(req.getInputStream(), type);
    }

    public void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(status);
        mapper.writeValue(resp.getWriter(), body);
    }

    public void writeError(HttpServletResponse resp, int status, String message) throws IOException {
        writeJson(resp, status, java.util.Map.of("error", message));
    }

    /** Extract the trailing numeric ID from a path like "/42" or null if absent. */
    public static Long parseId(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isBlank()) return null;
        String stripped = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
        try {
            return Long.parseLong(stripped);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

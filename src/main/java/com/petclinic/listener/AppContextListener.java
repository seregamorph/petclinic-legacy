package com.petclinic.listener;

import com.petclinic.config.DatabaseConfig;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Runs on application startup: executes Flyway migrations
 * and eagerly initialises all singletons so the first request
 * doesn't pay the initialisation cost.
 */
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseConfig db = DatabaseConfig.getInstance();
        db.runMigrations();
        // Warm up singleton chain: service -> repository -> DatabaseConfig
        com.petclinic.service.OwnerService.getInstance();
        com.petclinic.service.PetService.getInstance();
        com.petclinic.service.VetService.getInstance();
        com.petclinic.service.VisitService.getInstance();
        sce.getServletContext().log("PetClinic started – migrations applied.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("PetClinic stopped.");
    }
}

package com.petclinic.listener;

import com.petclinic.config.DatabaseConfig;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Runs on application startup: executes Flyway migrations
 * and eagerly initialises all singletons so the first request
 * doesn't pay the initialisation cost.
 */
@Component
public class AppContextListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        DatabaseConfig db = DatabaseConfig.getInstance();
        db.runMigrations();
        // Warm up singleton chain: service -> repository -> DatabaseConfig
        com.petclinic.service.OwnerService.getInstance();
        com.petclinic.service.PetService.getInstance();
        com.petclinic.service.VetService.getInstance();
        com.petclinic.service.VisitService.getInstance();
    }
}

package com.petclinic.spring;

import com.petclinic.PetclinicApplication;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringContextListener implements ServletContextListener {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var springApplication = new SpringApplication(PetclinicApplication.class);
        String[] args = {};
        applicationContext = springApplication.run(args);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        applicationContext.close();
    }
}

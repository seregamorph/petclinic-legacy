package com.petclinic.spring;

import com.petclinic.PetclinicApplication;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

@WebListener
public class SpringContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(SpringContextListener.class);

    // disabled if executed via PetclinicApplication.main
    public static final AtomicBoolean disabled = new AtomicBoolean();

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (!disabled.get()) {
            var springApplication = new SpringApplication(PetclinicApplication.class);
            String[] args = {};
            try {
                applicationContext = springApplication.run(args);
            } catch (Exception e) {
                logger.error("Error while loading spring context", e);
                throw e;
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (!disabled.get()) {
            applicationContext.close();
        }
    }
}

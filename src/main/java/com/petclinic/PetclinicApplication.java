package com.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;
//import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class
})
@ServletComponentScan
public class PetclinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetclinicApplication.class, args);
    }
}

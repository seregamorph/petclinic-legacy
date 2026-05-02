package com.petclinic.spring;

import com.petclinic.repository.OwnerRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        OwnerRepository.class
})
public class RepositoryConfiguration {
}

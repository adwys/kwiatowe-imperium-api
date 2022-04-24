package com.kwiatowe_imperium.api.utilis;

import com.kwiatowe_imperium.api.models.Role;
import com.kwiatowe_imperium.api.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInsert implements ApplicationRunner {

    @Autowired
    RoleRepository repository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(repository.findByName("ADMIN") == null){
            repository.save(new Role("ADMIN"));
        }
        if(repository.findByName("USER") == null){
            repository.save(new Role("USER"));
        }
    }
}

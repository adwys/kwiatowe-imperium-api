package com.kwiatowe_imperium.api.servies;

import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.repo.RoleRepository;
import com.kwiatowe_imperium.api.repo.UserRepository;
import com.kwiatowe_imperium.api.utilis.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository repository;
    private UserRepository userRepository;
    private JwtUtil util;

    public ResponseEntity<?> readAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<?> create(Role item){
        repository.save(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public ResponseEntity<?> read(Long id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }


    public ResponseEntity<?> delete(Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Role toReturn = repository.getById(id);
        repository.deleteById(id);
        return new ResponseEntity<>(toReturn, HttpStatus.OK);
    }

    public ResponseEntity<?> giveRole(String token){

        String jwt = token.substring(7);
        Optional<UserModel> source;
        try{
            source = userRepository.findByEmail(util.extractUsername(jwt));

        }catch (Exception e){
            return new ResponseEntity("bad token",HttpStatus.BAD_REQUEST);
        }

        Role role = repository.findByName("ADMIN");
        source.get().roles.add(role);
        repository.save(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

}

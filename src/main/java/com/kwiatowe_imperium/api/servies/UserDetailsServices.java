package com.kwiatowe_imperium.api.servies;

import com.kwiatowe_imperium.api.models.UserModel;
import com.kwiatowe_imperium.api.repo.UserRepository;
import com.kwiatowe_imperium.api.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServices implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtUtil util;

    public ResponseEntity updateUser(String token,UserModel model){
        String jwt = token.substring(7);
        Optional<UserModel> userModel;

        try{
            repository.findByEmail(util.extractUsername(jwt))
            .ifPresent(user -> {
                user.updateForm(model);
                repository.save(user);
            });

        }catch (Exception e){
            return new ResponseEntity("bad token",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(model,HttpStatus.OK);
    }

    public ResponseEntity getUserByToken(String token){
        Optional<UserModel> userModel;
        String[] p=token.split("\\s");
        try {
            String username = util.extractUsername(p[1]);
            userModel = repository.findByEmail(username);
        }catch (Exception e){
            return new ResponseEntity<>("bad token", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userModel,HttpStatus.OK);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel;
        if(repository.findByEmail(username).isPresent()){
            userModel = repository.findByEmail(username).get();
        }
        else{
            return null;
        }

        return new User(userModel.getEmail(), userModel.getPassword(), new ArrayList<>());
    }
}
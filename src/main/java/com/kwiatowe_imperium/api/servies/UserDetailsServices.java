package com.kwiatowe_imperium.api.servies;

import com.kwiatowe_imperium.api.models.Role;
import com.kwiatowe_imperium.api.models.UserModel;
import com.kwiatowe_imperium.api.repo.UserRepository;
import com.kwiatowe_imperium.api.utilis.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@AllArgsConstructor
public class UserDetailsServices implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtUtil util;


    public UserModel getUserFromJwt(String token){
        String jwt = token.substring(7);
        Optional<UserModel> userModel;

        try{
           userModel = repository.findByEmail(util.extractUsername(jwt));

        }catch (Exception e){
            return null;
        }

        return userModel.get();
    }

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

        return new User(userModel.getEmail(), userModel.getPassword(), getAuthority(userModel));
    }

    private Set<SimpleGrantedAuthority> getAuthority(UserModel user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }
}
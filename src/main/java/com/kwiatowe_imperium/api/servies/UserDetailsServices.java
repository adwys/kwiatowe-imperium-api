package com.kwiatowe_imperium.api.servies;

import com.kwiatowe_imperium.api.models.UserModel;
import com.kwiatowe_imperium.api.repo.UserRepository;
import com.kwiatowe_imperium.api.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServices implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtUtil util;

    public Optional<UserModel> getUserByToken(String token){
        String[] p=token.split("\\s");
        String username = util.extractUsername(p[1]);
        Optional<UserModel> userModel = repository.findByUsername(username);
        return userModel;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel;
        if(repository.findByUsername(username).isPresent()){
            userModel = repository.findByUsername(username).get();
        }
        else{
            return null;
        }

        return new User(userModel.getUsername(), userModel.getPassword(), new ArrayList<>());
    }
}
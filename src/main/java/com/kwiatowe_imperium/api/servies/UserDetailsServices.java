package com.kwiatowe_imperium.api.servies;

import com.kwiatowe_imperium.api.models.Email;
import com.kwiatowe_imperium.api.models.Role;
import com.kwiatowe_imperium.api.models.UserModel;
import com.kwiatowe_imperium.api.models.UserResponse;
import com.kwiatowe_imperium.api.repo.EmailRepository;
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
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserDetailsServices implements UserDetailsService {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private JwtUtil util;


    public UserModel jwtUser(String token){
        String jwt = token.substring(7);
        Optional<UserModel> userModel;

        try{
            userModel = repository.findByEmail(util.extractUsername(jwt));

        }catch (Exception e){
            return null;
        }

        return userModel.get();
    }


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

    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(repository.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUser(Long id){

        Optional<UserModel> userModel = repository.findById(id);
        if(userModel.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.delete(userModel.get());
        return new ResponseEntity<>(userModel.get(),HttpStatus.OK);
    }


    public ResponseEntity<?> MailSend(String jwt, Date time){

        UserModel userModel = jwtUser(jwt);

        Email email = new Email(
                null,
                time,
                "przypomnienie",
                "przypominam o urodzinkach xd",
                userModel.getEmail());
        emailRepository.save(email);

        return new ResponseEntity<>("date set",HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(String token,UserModel model){
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

    public ResponseEntity<?> getUserByToken(String token){
        UserModel userModel;
        String[] p=token.split("\\s");
        try {
            String username = util.extractUsername(p[1]);
            userModel = repository.findByEmail(username).get();
        }catch (Exception e){
            return new ResponseEntity<>("bad token", HttpStatus.BAD_REQUEST);
        }

        UserResponse user = new UserResponse(
                userModel.getId(),
                userModel.getName(),
                userModel.getSurname(),
                userModel.getAddress(),
                userModel.getPostalCode(),
                userModel.getCity(),
                userModel.getEmail(),
                userModel.getRoles(),
                userModel.getCart());

        return new ResponseEntity<>(user,HttpStatus.OK);
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
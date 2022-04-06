package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.repo.RoleRepository;
import com.kwiatowe_imperium.api.repo.UserRepository;
import com.kwiatowe_imperium.api.utilis.EmailValidation;
import com.kwiatowe_imperium.api.models.RegistrationRequest;
import com.kwiatowe_imperium.api.models.UserModel;
import com.kwiatowe_imperium.api.repo.UserRepository;
import com.kwiatowe_imperium.api.utilis.EmailValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final EmailValidation emailValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private UserDetailsServices userDetailsServices;
    public ResponseEntity<?> register(RegistrationRequest request) {
        Role role = roleRepository.findByName("USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        boolean isValid = emailValidator.patternMatches(request.getEmail());
        if(!isValid){
            return new ResponseEntity<>("Email is not valid", HttpStatus.BAD_REQUEST);
        }
        boolean userExist = userRepository.findByEmail(request.getEmail()).isPresent();
        if(userExist){
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        UserModel user = new UserModel(request.getName(),request.getSurname(),
                request.getEmail(),encodedPassword);
        user.setRoles(roleSet);
        userRepository.save(user);
        return new ResponseEntity<>("User registered", HttpStatus.OK);
    }

    public ResponseEntity<?> createAdmin(RegistrationRequest request) {
        Role role = roleRepository.findByName("ADMIN");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        boolean isValid = emailValidator.patternMatches(request.getEmail());
        if(!isValid){
            return new ResponseEntity<>("Email is not valid", HttpStatus.BAD_REQUEST);
        }
        boolean userExist = userRepository.findByEmail(request.getEmail()).isPresent();
        if(userExist){
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        UserModel user = new UserModel(request.getName(),request.getSurname(),
                request.getEmail(),encodedPassword);
        user.setRoles(roleSet);
        userRepository.save(user);
        return new ResponseEntity<>("User registered", HttpStatus.OK);
    }

    public ResponseEntity updatePassword(String jwt, ChangePasswordRequest request){
        UserModel userModel;
        try {
            userModel = userDetailsServices.getUserFromJwt(jwt);
        }catch (Exception e){
            return new ResponseEntity("bad token", HttpStatus.BAD_REQUEST);
        }

        if(!bCryptPasswordEncoder.matches(request.getOldPassword(),userModel.getPassword())){
            return new ResponseEntity("Wrong passwrod",HttpStatus.BAD_REQUEST);
        }
        String encoded = bCryptPasswordEncoder.encode(request.getNewPassword());
        userModel.setPassword(encoded);
        userRepository.save(userModel);
        return new ResponseEntity("password changed",HttpStatus.OK);
    }


}

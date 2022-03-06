package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.RegistrationRequest;
import com.kwiatowe_imperium.api.models.UserModel;
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

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final EmailValidation emailValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public ResponseEntity<?> register(RegistrationRequest request) {
        boolean isValid = emailValidator.patternMatches(request.getEmail());
        if(!isValid){
            return new ResponseEntity<>("Email is not valid", HttpStatus.BAD_REQUEST);
        }
        boolean userExist = userRepository.findByEmail(request.getEmail()).isPresent();
        if(userExist){
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        userExist = userRepository.findByUsername(request.getUsername()).isPresent();
        if(userExist){
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        UserModel user = new UserModel(request.getName(),request.getSurname(),request.getEmail(),request.getUsername(),encodedPassword);
        userRepository.save(user);
        return new ResponseEntity<>("User registered", HttpStatus.OK);
    }

}

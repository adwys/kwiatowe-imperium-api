package com.kwiatowe_imperium.api.controllers;

import com.kwiatowe_imperium.api.models.AuthenticationRequest;
import com.kwiatowe_imperium.api.models.AuthenticationResponse;
import com.kwiatowe_imperium.api.models.RegistrationRequest;
import com.kwiatowe_imperium.api.models.UserModel;
import com.kwiatowe_imperium.api.servies.RegistrationService;
import com.kwiatowe_imperium.api.servies.UserDetailsServices;
import com.kwiatowe_imperium.api.utilis.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class SecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServices userDetailsServices;

    @Autowired
    private JwtUtil jwtToken;

    @Autowired
    private final RegistrationService registrationService;


    @RequestMapping(value = "/auth/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())

            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password",e);
        }

        final UserDetails userDetails = userDetailsServices
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtToken.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }


    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerNewUser(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @RequestMapping(value = "/auth/me",method = RequestMethod.GET)
    private ResponseEntity<?> getUser(@RequestHeader("Authorization") String jwt){
        return userDetailsServices.getUserByToken(jwt);

    }

    @RequestMapping(value = "/auth/me",method = RequestMethod.PATCH)
    private ResponseEntity<?> updateUser(@RequestHeader("Authorization") String jwt, @RequestBody UserModel user){
        return userDetailsServices.updateUser(jwt,user);
    }

}

package com.kwiatowe_imperium.api.controllers;

import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.servies.RegistrationService;
import com.kwiatowe_imperium.api.servies.RoleService;
import com.kwiatowe_imperium.api.servies.UserDetailsServices;
import com.kwiatowe_imperium.api.utilis.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private final RoleService roleService;


    @RequestMapping(value = "/auth/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword())

            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password",e);
        }

        final UserDetails userDetails = userDetailsServices
                .loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtToken.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/userping", method = RequestMethod.GET)
    public String userPing(){
        return "only User Can Read This";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/Adminping", method = RequestMethod.GET)
    public String AdminPing(){
        return "only ADMIN Can Read This";
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerNewUser(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @RequestMapping(value = "/auth/registerAdmin", method = RequestMethod.POST)
    public ResponseEntity<?> registerAdmin(@RequestBody RegistrationRequest request){
        return registrationService.createAdmin(request);
    }


    @RequestMapping(value = "/auth/me",method = RequestMethod.GET)
    private ResponseEntity<?> getUser(@RequestHeader("Authorization") String jwt){
        return userDetailsServices.getUserByToken(jwt);
    }
    @RequestMapping(value = "/auth/me",method = RequestMethod.PUT)
    private ResponseEntity<?> setAdmin(@RequestHeader("Authorization") String jwt){
        return roleService.giveRole(jwt);
    }

    @RequestMapping(value = "/auth/me",method = RequestMethod.PATCH)
    private ResponseEntity<?> updateUser(@RequestHeader("Authorization") String jwt, @RequestBody UserModel user){
        return userDetailsServices.updateUser(jwt,user);
    }

    @RequestMapping(value = "/auth/me/passwordChange",method = RequestMethod.PATCH)
    private ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordRequest request){
        return registrationService.updatePassword(jwt,request);
    }

    @RequestMapping(value = "/users/all",method = RequestMethod.GET)
    private ResponseEntity<?> getUser(){
        return userDetailsServices.getAllUsers();
    }

    @RequestMapping(value = "/users/{id}",method = RequestMethod.DELETE)
    private ResponseEntity<?> deleteUser(@PathVariable Long id){
        return userDetailsServices.deleteUser(id);
    }

}

package com.kwiatowe_imperium.api.controllers;

import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.models.Role;
import com.kwiatowe_imperium.api.servies.ProductService;
import com.kwiatowe_imperium.api.servies.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class RoleController {

    private final RoleService service;


    @RequestMapping(value = "/api/role", method = RequestMethod.POST)
    public ResponseEntity<?> create( @RequestBody Role request){
        return service.create(request);
    }

    @RequestMapping(value = "/api/role/all", method = RequestMethod.GET)
    ResponseEntity<?> findAllProduct(){
        return service.readAll();
    }

    @RequestMapping(value = "/api/role/{id}", method = RequestMethod.GET)
    ResponseEntity<?> findProduct(@PathVariable Long id){
        return service.read(id);
    }

    @RequestMapping(value = "/api/role/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteProduct(@PathVariable Long id){
        return service.delete(id);
    }



}

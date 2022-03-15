package com.kwiatowe_imperium.api.controllers;


import com.kwiatowe_imperium.api.models.Image;
import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.servies.ImageService;
import com.kwiatowe_imperium.api.servies.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class ImageController {

    private final ImageService service;



    @RequestMapping(value = "/api/image/product/{product_id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductImages(@PathVariable Long product_id){
        return service.getAllProductImages(product_id);
    }

    @RequestMapping(value = "/api/image/db", method = RequestMethod.POST)
    public ResponseEntity<?> createDB(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name){
        return service.saveToDB(file,name);
    }

    @RequestMapping(value = "/api/image", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Image request){
        return service.create(request);
    }
    @RequestMapping(value = "/api/image/all", method = RequestMethod.GET,params = {"!sort", "!page", "!size"})
    ResponseEntity<?> findAll(){
        return service.readAll();
    }

    @RequestMapping(value = "/api/image/{id}", method = RequestMethod.GET)
    ResponseEntity<?> find(@PathVariable Long id){
        return service.read(id);
    }

    @RequestMapping(value = "/api/image/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@PathVariable Long id){
        return service.delete(id);
    }

    @RequestMapping(value = "/api/image/{id}", method = RequestMethod.PATCH)
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody Image request){
        return service.update(id,request);
    }

}

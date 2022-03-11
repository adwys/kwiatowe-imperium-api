package com.kwiatowe_imperium.api.controllers;


import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.servies.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    @RequestMapping(value = "/api/product/{product_id}/image/{image_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> addTo(
            @PathVariable Long product_id,
            @PathVariable Long image_id
    ){
        return new ResponseEntity<>(service.addImageToProduct(product_id,image_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/product", method = RequestMethod.POST)
    public ResponseEntity<?> createProduct(@RequestBody Product request){
        return service.createProduct(request);
    }
    @RequestMapping(value = "/api/product/all", method = RequestMethod.GET,params = {"!sort", "!page", "!size"})
    ResponseEntity<?> findAllProduct(){
        return service.readAllProduct();
    }

    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.GET)
    ResponseEntity<?> findProduct(@PathVariable Long id){
        return service.readProduct(id);
    }

    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteProduct(@PathVariable Long id){
        return service.deleteProduct(id);
    }

    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.PATCH)
    ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product){
        return service.updateProduct(id,product);
    }

}

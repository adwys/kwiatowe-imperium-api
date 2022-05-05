package com.kwiatowe_imperium.api.controllers;


import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.models.ProductRequest;
import com.kwiatowe_imperium.api.servies.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    @RequestMapping(value = "/api/product/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> readByName(@PathVariable String name){
        return service.readByName(name);
    }

    @RequestMapping(value = "/api/product/{product_id}/image/{image_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> addTo(
            @PathVariable Long product_id,
            @PathVariable Long image_id
    ){
        return new ResponseEntity<>(service.addImageToProduct(product_id,image_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/product", method = RequestMethod.POST)
    public ResponseEntity<?> createProduct( @RequestBody ProductRequest request){
        return service.create(request);
    }

    @RequestMapping(value = "/api/product/all", method = RequestMethod.GET)
    ResponseEntity<?> findAllProduct(@RequestParam(value = "page",required = false,defaultValue = "0") Integer page,
                                     @RequestParam(value = "size",required = false,defaultValue = "10") Integer size,
                                     @RequestParam(value = "cat",required = false,defaultValue = "-1") Long cat,
                                     @RequestParam(value = "catName",required = false,defaultValue = "none") String catName,
                                     @RequestParam(value = "sort",required = false,defaultValue = "id") String sortBy,
                                     @RequestHeader("accept-language") String lang){
        return service.readAllProduct(page,size,cat,catName,sortBy,lang);
    }

    @RequestMapping(value = "/api/product/full/{id}",method = RequestMethod.GET)
    ResponseEntity<?> readFullProduct(@PathVariable Long id,@RequestHeader("accept-language") String lang){
        return service.readFullProduct(id,lang);
    }

    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.GET)
    ResponseEntity<?> findProduct(@PathVariable Long id,@RequestHeader("accept-language") String lang){
        return service.read(id,lang);
    }

    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteProduct(@PathVariable Long id){
        return service.delete(id);
    }

    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.PATCH)
    ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductRequest product){
        return service.update(id,product);
    }

}

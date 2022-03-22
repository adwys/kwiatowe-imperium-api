package com.kwiatowe_imperium.api.controllers;


import com.kwiatowe_imperium.api.models.Category;
import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.servies.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @RequestMapping(value = "/api/category/{category_id}/product/{product_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> addTo(
            @PathVariable Long product_id,
            @PathVariable Long category_id
    ){
        return new ResponseEntity<>(service.addTo(category_id,product_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/category", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Category request){
        return service.create(request);
    }
    @RequestMapping(value = "/api/category/all", method = RequestMethod.GET,params = {"!sort", "!page", "!size"})
    ResponseEntity<?> findAll(@RequestHeader("accept-language") String lang){
        return service.readAll(lang);
    }

    @RequestMapping(value = "/api/category/{id}", method = RequestMethod.GET)
    ResponseEntity<?> find(@PathVariable Long id, @RequestHeader("accept-language") String lang){
        return service.read(id,lang);
    }

    @RequestMapping(value = "/api/category/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@PathVariable Long id){
        return service.delete(id);
    }

    @RequestMapping(value = "/api/category/{id}", method = RequestMethod.PATCH)
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody Category category){
        return service.update(id,category);
    }

}

package com.kwiatowe_imperium.api.controllers;

import com.kwiatowe_imperium.api.models.Category;
import com.kwiatowe_imperium.api.models.Hero;
import com.kwiatowe_imperium.api.servies.CategoryService;
import com.kwiatowe_imperium.api.servies.HeroService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class HeroController {

    private final HeroService service;

    @RequestMapping(value = "/api/hero/{hero_id}/image/{image_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> addTo(
            @PathVariable Long hero_id,
            @PathVariable Long image_id
    ){
        return new ResponseEntity<>(service.addTo(hero_id,image_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/hero", method = RequestMethod.GET)
    public ResponseEntity<?> getHero(@RequestHeader("accept-language") String lang){
        return service.getHero(lang);
    }

    @RequestMapping(value = "/api/hero", method = RequestMethod.POST)
    public ResponseEntity<?> createMain(@RequestBody Hero request){
        return service.createMain(request);
    }

    @RequestMapping(value = "/api/hero", method = RequestMethod.DELETE)
    ResponseEntity<?> delete(){
        return service.deleteMain();
    }

    @RequestMapping(value = "/api/hero", method = RequestMethod.PATCH)
    ResponseEntity<?> update(@RequestBody Hero hero){
        return service.updateMain(hero);
    }

    @RequestMapping(value = "/api/hero/add", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Hero request){
        return service.create(request);
    }

    @RequestMapping(value = "/api/hero/all", method = RequestMethod.GET,params = {"!sort", "!page", "!size"})
    ResponseEntity<?> findAll(@RequestHeader("accept-language") String lang){
        return service.readAll(lang);
    }

    @RequestMapping(value = "/api/hero/{id}", method = RequestMethod.GET)
    ResponseEntity<?> find(@PathVariable Long id, @RequestHeader("accept-language") String lang){
        return service.read(id,lang);
    }

    @RequestMapping(value = "/api/hero/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@PathVariable Long id){
        return service.delete(id);
    }

    @RequestMapping(value = "/api/hero/{id}", method = RequestMethod.PATCH)
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody Hero hero){
        return service.update(id,hero);
    }
}

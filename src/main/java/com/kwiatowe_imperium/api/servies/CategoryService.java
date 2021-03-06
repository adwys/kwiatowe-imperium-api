package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.repo.CategoryRepository;
import com.kwiatowe_imperium.api.repo.HeroRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository repository;

    private ProductRepository productRepository;

    private HeroRepository heroRepository;

    public ResponseEntity<?> readByName(String lang,String name){

        try {
            if (repository.findByNameEnIgnoreCase(name) != null) {
                if(lang.equals("pl")){
                    return new ResponseEntity<>(MapToPl(repository.findByNameEnIgnoreCase(name))
                            , HttpStatus.OK);
                }
                return new ResponseEntity<>(MapToEng(repository.findByNameEnIgnoreCase(name))
                        , HttpStatus.OK);
            }
            if (repository.findByNamePlIgnoreCase(name) != null) {
                if(lang.equals("en")){
                    return new ResponseEntity<>(MapToEng(repository.findByNamePlIgnoreCase(name))
                            , HttpStatus.OK);
                }
                return new ResponseEntity<>(MapToPl(repository.findByNamePlIgnoreCase(name)), HttpStatus.OK);
            }
        }catch (Exception e){
        return new ResponseEntity<>("more than one item with this name",HttpStatus.BAD_REQUEST);
    }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<?> readCategortFull(Long id){
        return new ResponseEntity<>(repository.findById(id),HttpStatus.OK);
    }

    public ResponseEntity<?> readAllIfVisible(String lang) {
        if(lang.equals("en")){
            return new ResponseEntity<>(repository.findAllByVisibleIsTrue()
                    .stream()
                    .map(CategoryService::MapToEng)
                    .collect(Collectors.toList()),HttpStatus.OK);
        }
        return new ResponseEntity<>(repository.findAllByVisibleIsTrue()
                .stream()
                .map(CategoryService::MapToPl)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    public ResponseEntity<?> readAll(String lang) {
        if(lang.equals("en")){
            return new ResponseEntity<>(repository.findAll()
                    .stream()
                    .map(CategoryService::MapToEng)
                    .collect(Collectors.toList()),HttpStatus.OK);
        }
        return new ResponseEntity<>(repository.findAll()
                .stream()
                .map(CategoryService::MapToPl)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    public ResponseEntity<?> addTo(Long parent_id, Long child_id){
        Product source = productRepository.findById(child_id).get();
        Category category = repository.findById(parent_id).get();
        category.products.add(source);
        source.categories.add(category);
        repository.save(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    public ResponseEntity<?> detach(Long parent_id, Long child_id){
        Product source = productRepository.findById(child_id).get();
        Category category = repository.findById(parent_id).get();
        category.products.remove(source);
        source.categories.remove(category);
        repository.save(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    public ResponseEntity<?> create(Category item) {
        repository.save(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public ResponseEntity<?> read(Long id,String lang) {
        if(lang.equals("en")){
            return new ResponseEntity<>(
                    repository.findById(id)
                    .map(CategoryService::MapToEng)
                    ,HttpStatus.OK);
        }
        return new ResponseEntity<>(
                repository.findById(id)
                .map(CategoryService::MapToPl)
                ,HttpStatus.OK);
    }

    public ResponseEntity<?> update(Long id, Category toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Category model;
        try {
            model = toUpdate;
        } catch (Exception e) {
            return new ResponseEntity<>("bad body", HttpStatus.BAD_REQUEST);
        }

        repository.findById(id)
                .ifPresent(category -> {
                    category.updateFrom(model);
                    repository.save(category);
                });
        return new ResponseEntity<>(model, HttpStatus.OK);

    }

    public ResponseEntity<?> delete(Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if(heroRepository.findByCategoryId(id) != null){
            heroRepository.findByCategoryId(id).setCategory(null);
        }
//        Category toReturn = repository.getById(id);
        repository.deleteById(id);
        return new ResponseEntity<>("deleted", HttpStatus.OK);

    }

    public static CategoryDTO MapToPl(Category c){
        return new CategoryDTO(
                c.getId(),
                c.getNamePl(),
                c.isVisible());
//                c.getProducts().stream()
//                        .map(ProductService::MapToPl)
//                        .collect(Collectors.toList()));
    }

    public static CategoryDTO MapToEng(Category c){
        return new CategoryDTO(
                c.getId(),
                c.getNameEn(),
                c.isVisible());
//                c.getProducts().stream()
//                        .map(ProductService::MapToEng)
//                        .collect(Collectors.toList()));
    }


}

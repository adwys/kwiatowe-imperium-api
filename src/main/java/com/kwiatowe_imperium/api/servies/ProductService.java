package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.Image;
import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.models.ProductDTO;
import com.kwiatowe_imperium.api.repo.ImageRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ImageRepository imageRepository;

    public ResponseEntity<?> readByName(String name){
        try {
            if(repository.findByNameEnIgnoreCase(name) != null){
                return new ResponseEntity<>(MapToEng(repository.findByNameEnIgnoreCase(name)), HttpStatus.OK);
            }
            if(repository.findByNamePlIgnoreCase(name) != null){
                return new ResponseEntity<>(MapToPl(repository.findByNamePlIgnoreCase(name)), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>("more than one item with this name",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public  ResponseEntity<?> readAllProduct(String lang){
        if(lang.equals("en")){
            return new ResponseEntity<>(repository.findAll()
                    .stream()
                    .map(ProductService::MapToEng)
                    .collect(Collectors.toList()),HttpStatus.OK);
        }
        return new ResponseEntity<>(repository.findAll()
                .stream()
                .map(ProductService::MapToPl)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    public static ProductDTO MapToPl(Product p){

        return new ProductDTO(p.getId(),p.getNamePl(),p.getDescriptionPl(),p.getPrice(),p.getImages());
    }

    public static ProductDTO MapToEng(Product p){
        if(p.getDescriptionEn() == null)
            return new ProductDTO(p.getId(),p.getNameEn(),p.getDescriptionPl(),p.getPrice(),p.getImages());
        return new ProductDTO(p.getId(),p.getNameEn(),p.getDescriptionEn(),p.getPrice(),p.getImages());
    }

    public ResponseEntity<?> addImageToProduct(Long parent_id, Long child_id){
        Image sourceImage = imageRepository.findById(child_id).get();
        Product product = repository.findById(parent_id).get();
        sourceImage.setProduct(product);
        imageRepository.save(sourceImage);
        return new ResponseEntity<>(sourceImage, HttpStatus.OK);
    }



    public ResponseEntity<?> create(Product product){
        repository.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    public ResponseEntity<?> read(Long id, String lang){
        if(lang.equals("en")){
            return new ResponseEntity<>(
                    repository.findById(id)
                    .map(ProductService::MapToEng)
                    ,HttpStatus.OK);
        }
        return new ResponseEntity<>(
                repository.findById(id)
                .map(ProductService::MapToPl)
                ,HttpStatus.OK);
    }

    public ResponseEntity<?> update(Long id, Product toUpdate){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Product model;
        try {
            model = toUpdate;
        }catch (Exception e){
            return new ResponseEntity<>("bad body",HttpStatus.BAD_REQUEST);
        }

        repository.findById(id)
                .ifPresent(product -> {
                    product.updateFrom(model);
                    repository.save(product);
                });
        return new ResponseEntity<>(model, HttpStatus.OK);

    }
    public ResponseEntity<?> delete(Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Product toReturn = repository.getById(id);
        repository.deleteById(id);
        return new ResponseEntity<>(toReturn, HttpStatus.OK);
    }

}

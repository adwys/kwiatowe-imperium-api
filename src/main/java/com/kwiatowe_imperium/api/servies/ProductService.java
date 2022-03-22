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

    public  ResponseEntity<?> readAllProduct(String lang){
        if(lang.equals("eng")){
            return new ResponseEntity<>(repository.findAll()
                    .stream()
                    .map(this::MapToEng)
                    .collect(Collectors.toList()),HttpStatus.OK);
        }
        return new ResponseEntity<>(repository.findAll()
                .stream()
                .map(this::MapToPl)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    private ProductDTO MapToPl(Product p){
        return new ProductDTO(p.getId(),p.getNamePl(),p.getDescriptionPl(),p.getPrice(),p.getImages());
    }

    private ProductDTO MapToEng(Product p){
        return new ProductDTO(p.getId(),p.getNameEn(),p.getDescriptionEn(),p.getPrice(),p.getImages());
    }

    public ResponseEntity<?> addImageToProduct(Long parent_id, Long child_id){
        Image sourceImage = imageRepository.findById(child_id).get();
        Product product = repository.findById(parent_id).get();
        sourceImage.setProduct(product);
        imageRepository.save(sourceImage);
        return new ResponseEntity<>(sourceImage, HttpStatus.OK);
    }



    public ResponseEntity<?> createProduct(Product product){
        repository.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    public ResponseEntity<?> readProduct(Long id,String lang){
        if(lang.equals("eng")){
            return new ResponseEntity<>(repository.findById(id)
                    .stream()
                    .map(this::MapToEng)
                    .collect(Collectors.toList()),HttpStatus.OK);
        }
        return new ResponseEntity<>(repository.findById(id)
                .stream()
                .map(this::MapToPl)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    public ResponseEntity<?> updateProduct(Long id,Product toUpdate){
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
    public ResponseEntity<?> deleteProduct(Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Product toReturn = repository.getById(id);
        repository.deleteById(id);
        return new ResponseEntity<>(toReturn, HttpStatus.OK);
    }

}

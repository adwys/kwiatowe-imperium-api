package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.repo.CategoryRepository;
import com.kwiatowe_imperium.api.repo.ImageRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import lombok.AllArgsConstructor;
import netscape.javascript.JSObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ImageRepository imageRepository;

    private final CategoryRepository categoryRepository;

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

    public  ResponseEntity<?> readAllProduct(int page,int size,Long cat,String lang){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products;
        products = repository.findAll(pageable);
        if(categoryRepository.existsById(cat)){
            products = new PageImpl<>(categoryRepository.findById(cat).get().products);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        if(lang.equals("en")){
            map.put("count",repository.count());
            map.put("data",products.stream()
                    .map(ProductService::MapToEng)
                    .collect(Collectors.toList()));
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        else {
            map.put("count",repository.count());
            map.put("data",products.stream()
                    .map(ProductService::MapToPl)
                    .collect(Collectors.toList()));
            return new ResponseEntity<>(map,HttpStatus.OK);
        }

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



    public ResponseEntity<?> create(ProductRequest request){
        Product product = new Product(
                null,
                request.getNameEn(),
                request.getNamePl(),
                request.getDescriptionEn(),
                request.getDescriptionPl(),
                request.getPrice(),
                null,
                null
                );

        if(request.getImages()!=null){
            List<Image> images = new LinkedList<>();
            for(int i=0;i<request.getImages().size();i++){
                if(imageRepository.existsById(request.getImages().get(i))){
                    images.add(imageRepository.findById(request.getImages().get(i)).get());
                }
            }
            product.setImages(images);
        }
        if(request.getCategories()!=null){
            List<Category> categories = new LinkedList<>();
            for(int i=0;i<request.getCategories().size();i++){
                if(categoryRepository.existsById(request.getCategories().get(i))){
                    categories.add(categoryRepository.findById(request.getCategories().get(i)).get());
                }
            }
            product.setCategories(categories);
        }


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

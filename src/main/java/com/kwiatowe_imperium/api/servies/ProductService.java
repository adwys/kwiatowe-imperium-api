package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.repo.CategoryRepository;
import com.kwiatowe_imperium.api.repo.ImageRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import lombok.AllArgsConstructor;
import netscape.javascript.JSObject;
import org.springframework.data.domain.*;
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

    private final CategoryService categoryService;

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

    public ResponseEntity<?> readFullProduct(Long id,String lang){
        if(!repository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Product product = repository.findById(id).get();
        ProductReturn productReturn;
        if(lang.equals("en")){
            productReturn = new ProductReturn(
                    product.getNameEn(),
                    product.getNamePl(),
                    product.getDescriptionEn(),
                    product.getDescriptionPl(),
                    product.getPrice(),
                    product.images,
                    product.categories.stream().map(CategoryService::MapToEng).collect(Collectors.toList())
            );
        }
        else {
            productReturn = new ProductReturn(
                    product.getNameEn(),
                    product.getNamePl(),
                    product.getDescriptionEn(),
                    product.getDescriptionPl(),
                    product.getPrice(),
                    product.images,
                    product.categories.stream().map(CategoryService::MapToPl).collect(Collectors.toList())
            );
        }
        return new ResponseEntity<>(productReturn,HttpStatus.OK);
    }

    public  ResponseEntity<?> readAllProduct(int page,int size,Long cat,String catName,String sortBy,String querry,String lang){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Product> products;
        Long count = repository.count();
        products = repository.findAll(pageable);

        if(categoryRepository.existsById(cat)){
            count = categoryRepository.findById(cat).get().products.stream().count();
            products = new PageImpl<>(categoryRepository.findById(cat).get().products);
        }
        if(categoryRepository.findByNamePlIgnoreCase(catName) != null){
            count = categoryRepository.findByNamePlIgnoreCase(catName).products.stream().count();
            products = new PageImpl<>(categoryRepository.findByNamePlIgnoreCase(catName).products);
        }
        else if(categoryRepository.findByNameEnIgnoreCase(catName) != null){
            count = categoryRepository.findByNameEnIgnoreCase(catName).products.stream().count();
            products = new PageImpl<>(categoryRepository.findByNameEnIgnoreCase(catName).products);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        if(lang.equals("en")){


            if(!querry.equals("none")){
                map.put("data",repository.findByNameEnContaining(querry,pageable)
                .map(ProductService::MapToEng).stream().collect(Collectors.toList()));
                map.put("count",repository.findByNameEnContaining(querry,pageable)
                        .map(ProductService::MapToEng).stream().collect(Collectors.toList()).size());
            }
            else {
                map.put("data",products.stream()
                        .map(ProductService::MapToEng)
                        .collect(Collectors.toList()));
                map.put("count",count);
            }

            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        else {

            if(!querry.equals("none")){
                map.put("data",repository.findByNamePlContaining(querry,pageable)
                .map(ProductService::MapToPl).stream().collect(Collectors.toList()));
                map.put("count",repository.findByNamePlContaining(querry,pageable)
                        .map(ProductService::MapToPl).stream().collect(Collectors.toList()).size());
            }
            else {
                map.put("data",products.stream()
                        .map(ProductService::MapToPl)
                        .collect(Collectors.toList()));
                map.put("count",count);
            }
            return new ResponseEntity<>(map,HttpStatus.OK);
        }

    }

    public static ProductDTO MapToPl(Product p){

        return new ProductDTO(
                p.getId(),
                p.getNamePl(),
                p.getDescriptionPl(),
                p.getPrice(),
                p.getImages(),
                p.getCategories());
    }

    public static ProductDTO MapToEng(Product p){
        return new ProductDTO(
                p.getId(),
                p.getNameEn(),
                p.getDescriptionEn(),
                p.getPrice(),
                p.getImages(),
                p.getCategories());
    }

    public static ProductFullMap FullMaptoPl(Product p){
        return new ProductFullMap(
                p.getId(),
                p.getNamePl(),
                p.getDescriptionPl(),
                p.getPrice(),
                p.getImages(),
                p.getCategories().stream().map(CategoryService::MapToPl).collect(Collectors.toList()));
    }
    public static ProductFullMap FullMaptoEn(Product p){
        return new ProductFullMap(
                p.getId(),
                p.getNameEn(),
                p.getDescriptionEn(),
                p.getPrice(),
                p.getImages(),
                p.getCategories().stream().map(CategoryService::MapToEng).collect(Collectors.toList()));
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
                    imageRepository.findById(request.getImages().get(i)).get().setProduct(product);
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
        if(!repository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(lang.equals("en")){
            return new ResponseEntity<>(
                    repository.findById(id).map(ProductService::FullMaptoEn)
                    ,HttpStatus.OK);
        }
        return new ResponseEntity<>(
                repository.findById(id).map(ProductService::FullMaptoPl)
                ,HttpStatus.OK);
    }

    public ResponseEntity<?> update(Long id, ProductRequest request) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Product product = repository.findById(id).get();
        product.setNameEn(request.getNameEn());
        product.setNamePl(request.getNamePl());
        product.setPrice(request.getPrice());
        product.setDescriptionEn(request.getDescriptionEn());
        product.setDescriptionPl(request.getDescriptionPl());

        List<Image> imageList = imageRepository.findByProductId(product.getId());
        for(int i =0;i<imageList.size();i++){
            imageList.get(i).setProduct(null);
        }

        if (request.getImages() != null) {
            List<Image> images = new LinkedList<>();
            for (int i = 0; i < request.getImages().size(); i++) {
                if (imageRepository.existsById(request.getImages().get(i))) {
                    imageRepository.findById(request.getImages().get(i)).get().setProduct(product);
                    images.add(imageRepository.findById(request.getImages().get(i)).get());

                }
            }
            product.setImages(images);
        }


        for(int i =0;i<product.categories.size();i++){
            Category category = categoryRepository.findById(product.categories.get(i).getId()).get();
            categoryService.detach(category.getId(),product.getId());
//            category.products.remove(product.getId());
//            categoryRepository.save(category);
        }


        if (request.getCategories() != null) {
            List<Category> categories = new LinkedList<>();
            for (int i = 0; i < request.getCategories().size(); i++) {
                if (categoryRepository.existsById(request.getCategories().get(i))) {
                    categoryRepository.findById(request.getCategories().get(i)).get().products.add(product);
                    categories.add(categoryRepository.findById(request.getCategories().get(i)).get());
                }
            }
            product.setCategories(categories);
        }

            repository.save(product);
            return new ResponseEntity<>(product, HttpStatus.OK);
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

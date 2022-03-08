package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.Image;
import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.repo.ImageRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ImageRepository imageRepository;

    public  ResponseEntity<?> readAllProduct(){
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<?> addTo(Long parent_id, Long child_id){
        Image image = imageRepository.findById(child_id).get();
        Product product = repository.findById(parent_id).get();
        product.images.add(image);
//        image.setProduct(product);
        updateProduct(parent_id,product);
//        imageRepository.save(image);
//        return ResponseEntity.ok();
        return new ResponseEntity<>(repository.findById(parent_id), HttpStatus.OK);
    }



    public ResponseEntity<?> createProduct(Product product){
        repository.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    public ResponseEntity<?> readProduct(Long id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
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

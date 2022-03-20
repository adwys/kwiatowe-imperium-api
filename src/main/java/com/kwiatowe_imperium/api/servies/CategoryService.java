package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.Category;
import com.kwiatowe_imperium.api.models.Image;
import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.repo.CategoryRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository repository;

    private ProductRepository productRepository;

    public ResponseEntity<?> readAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<?> addTo(Long parent_id, Long child_id){
        Product source = productRepository.findById(child_id).get();
        Category category = repository.findById(parent_id).get();
        source.setCategory(category);
        productRepository.save(source);
        return new ResponseEntity<>(source, HttpStatus.OK);
    }

    public ResponseEntity<?> create(Category item) {
        repository.save(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public ResponseEntity<?> read(Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
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
        Category toReturn = repository.getById(id);
        repository.deleteById(id);
        return new ResponseEntity<>(toReturn, HttpStatus.OK);

    }
}

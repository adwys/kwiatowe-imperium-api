package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.Image;
import com.kwiatowe_imperium.api.models.Product;
import com.kwiatowe_imperium.api.repo.ImageRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository repository;

    public String loadImage(Long id){
        Image image = repository.findById(id).get();

        return "<img src=\"data:image/png;base64, "+ image.getImg() +"\">";
    }

    public ResponseEntity<?> saveToDB(MultipartFile file){
        Image image = new Image();
        try{
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            image.setImg(Base64.getEncoder().encodeToString(file.getBytes()));
        }catch (Exception e){
            System.out.println("err");
        }
        repository.save(image);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllProductImages(Long product_id){
        List<Image> imgs = repository.findByProductId(product_id);
        return new ResponseEntity<>(imgs, HttpStatus.OK);
    }

    public ResponseEntity<?> readAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<?> create(Image item){
        repository.save(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public ResponseEntity<?> read(Long id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    public ResponseEntity<?> update(Long id,Image toUpdate){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Image model;
        try {
            model = toUpdate;
        }catch (Exception e){
            return new ResponseEntity<>("bad body",HttpStatus.BAD_REQUEST);
        }

        repository.findById(id)
                .ifPresent(image -> {
                    image.updateFrom(model);
                    repository.save(image);
                });
        return new ResponseEntity<>(model, HttpStatus.OK);

    }
    public ResponseEntity<?> delete(Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Image toReturn = repository.getById(id);
        repository.deleteById(id);
        return new ResponseEntity<>(toReturn, HttpStatus.OK);
    }

}

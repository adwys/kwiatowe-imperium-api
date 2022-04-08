package com.kwiatowe_imperium.api.servies;

import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.repo.CategoryRepository;
import com.kwiatowe_imperium.api.repo.HeroRepository;
import com.kwiatowe_imperium.api.repo.ImageRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HeroService {

    private HeroRepository repository;

    private ImageRepository imageRepository;

    public ResponseEntity<?> readAll(String lang) {
        if(lang.equals("en")){
            return new ResponseEntity<>(repository.findAll()
                    .stream()
                    .map(HeroService::MapToEng)
                    .collect(Collectors.toList()),HttpStatus.OK);
        }
        return new ResponseEntity<>(repository.findAll()
                .stream()
                .map(HeroService::MapToPl)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    public ResponseEntity<?> addTo(Long parent_id, Long child_id){
        Image source = imageRepository.findById(child_id).get();
        Hero hero = repository.findById(parent_id).get();
        hero.setImage(source);
        repository.save(hero);
        return new ResponseEntity<>(hero, HttpStatus.OK);
    }

    public ResponseEntity<?> create(Hero item) {
        repository.save(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public ResponseEntity<?> createMain(Hero item) {
        if(repository.findByMain()==null){
            item.setMain(true);
            repository.save(item);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }
        Hero previous = repository.findByMain();
        previous.setMain(false);
        item.setMain(true);
        repository.save(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public ResponseEntity<?> read(Long id,String lang) {
        if(lang.equals("en")){
            return new ResponseEntity<>(
                    repository.findById(id)
                            .map(HeroService::MapToEng)
                    ,HttpStatus.OK);
        }
        return new ResponseEntity<>(
                repository.findById(id)
                        .map(HeroService::MapToPl)
                ,HttpStatus.OK);
    }

    public ResponseEntity<?> getHero(String lang) {
        Hero hero = repository.findByMain();

        try{
            if(lang.equals("en")){
                return new ResponseEntity<>(MapToEng(hero),HttpStatus.OK);
            }
            return new ResponseEntity<>(MapToPl(hero),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Hero not set yet",HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> updateMain(Long image_id,Hero toUpdate) {

        if (repository.findByMain() == null) {
            return createMain(toUpdate);
        }
        Hero model;
        try {
            model = toUpdate;
        } catch (Exception e) {
            return new ResponseEntity<>("bad body", HttpStatus.BAD_REQUEST);
        }

        Hero main = repository.findByMain();
        main.updateFrom(model);
        if(image_id != null){
            if(imageRepository.findById(image_id).isPresent()){
                addTo(main.getId(),image_id);
            }
        }
        repository.save(main);
        return new ResponseEntity<>(main, HttpStatus.OK);

    }


    public ResponseEntity<?> update(Long id, Hero toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Hero model;
        try {
            model = toUpdate;
        } catch (Exception e) {
            return new ResponseEntity<>("bad body", HttpStatus.BAD_REQUEST);
        }

        repository.findById(id)
                .ifPresent(hero -> {
                    hero.updateFrom(model);
                    repository.save(hero);
                });
        return new ResponseEntity<>(model, HttpStatus.OK);

    }

    public ResponseEntity<?> delete(Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Hero toReturn = repository.getById(id);
        repository.deleteById(id);
        return new ResponseEntity<>(toReturn, HttpStatus.OK);

    }

    public ResponseEntity<?> deleteMain() {
        if (repository.findByMain() == null) {
            return ResponseEntity.notFound().build();
        }
        Hero toReturn = repository.findByMain();
        repository.deleteById(toReturn.getId());
        return new ResponseEntity<>(toReturn, HttpStatus.OK);

    }

    public static HeroDTO MapToPl(Hero h){
        return new HeroDTO(
                h.getId(),
                h.getTitlePl(),
                h.getSubtitlePl(),
                h.getButtonTextPl(),
                h.getImage());
    }

    public static HeroDTO MapToEng(Hero h){
        return new HeroDTO(
                h.getId(),
                h.getTitleEn(),
                h.getSubtitleEn(),
                h.getButtonTextEn(),
                h.getImage());
    }

}

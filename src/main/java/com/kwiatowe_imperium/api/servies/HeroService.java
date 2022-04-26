package com.kwiatowe_imperium.api.servies;

import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.repo.CategoryRepository;
import com.kwiatowe_imperium.api.repo.HeroRepository;
import com.kwiatowe_imperium.api.repo.ImageRepository;
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
    private CategoryRepository categoryRepository;

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

    private Hero mapToHero(HeroRequest item){
        Hero hero = null;
        try {
            hero = new Hero(
                    item.getId(),
                    true,
                    item.getTitlePl(),
                    item.getSubtitlePl(),
                    item.getButtonTextPl(),
                    item.getTitleEn(),
                    item.getSubtitleEn(),
                    item.getButtonTextEn(),
                    null,
                    null
            );
            if(categoryRepository.existsById(item.getCategory())){
                hero.setCategory(categoryRepository.getById(item.getCategory()));
            }
            if(imageRepository.existsById(item.getImage())){
                hero.setImage(imageRepository.getById(item.getImage()));
            }
        }catch (Exception e){
            return null;
        }
        return hero;
    }

    public ResponseEntity<?> createMain(HeroRequest item) {

        Hero hero = mapToHero(item);
        if(hero == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(repository.findByMain()==null){
            repository.save(hero);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }
        Hero previous = repository.findByMain();
        previous.setMain(false);

        repository.save(hero);
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

    public ResponseEntity<?> getFullHero(String lang){
        Hero hero = repository.findByMain();
        HeroReturn heroReturn;
        if(lang.equals("pl")){
            CategoryHero h = null;
            if(hero.getCategory()!=null){
                h = new CategoryHero(hero.getCategory().getId(),hero.getCategory().getNamePl(),hero.getCategory().isVisible());
            }

            heroReturn = new HeroReturn(
                    hero.getId(),
                    hero.isMain(),
                    hero.getTitlePl(),
                    hero.getSubtitlePl(),
                    hero.getButtonTextPl(),
                    hero.getTitleEn(),
                    hero.getSubtitleEn(),
                    hero.getButtonTextEn(),
                    hero.getImage(),
                    h
                    );
        }else{
            CategoryHero h = null;
            if(hero.getCategory() != null){
                h = new CategoryHero(hero.getCategory().getId(),hero.getCategory().getNameEn(),hero.getCategory().isVisible());
            }
            heroReturn = new HeroReturn(
                    hero.getId(),
                    hero.isMain(),
                    hero.getTitlePl(),
                    hero.getSubtitlePl(),
                    hero.getButtonTextPl(),
                    hero.getTitleEn(),
                    hero.getSubtitleEn(),
                    hero.getButtonTextEn(),
                    hero.getImage(),
                    h);
        }

        return new ResponseEntity<>(heroReturn,HttpStatus.OK);
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

    public ResponseEntity<?> updateMain(HeroRequest request) {

        Hero toUpdate = mapToHero(request);
        if(toUpdate == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (repository.findByMain() == null) {
            return createMain(request);
        }
        Hero model;
        try {
            model = toUpdate;
        } catch (Exception e) {
            return new ResponseEntity<>("bad body", HttpStatus.BAD_REQUEST);
        }

        Hero main = repository.findByMain();
        main.updateFrom(model);
        if(categoryRepository.existsById(request.getCategory())){
            main.setCategory(categoryRepository.getById(request.getCategory()));
        }
        if(imageRepository.existsById(request.getImage())){
            main.setImage(imageRepository.getById(request.getImage()));
        }
        repository.save(main);
        return new ResponseEntity<>(request, HttpStatus.OK);

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
                h.getImage(),
                h.getCategory());
    }

    public static HeroDTO MapToEng(Hero h){
        return new HeroDTO(
                h.getId(),
                h.getTitleEn(),
                h.getSubtitleEn(),
                h.getButtonTextEn(),
                h.getImage(),
                h.getCategory());
    }

}

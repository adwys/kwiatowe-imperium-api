package com.kwiatowe_imperium.api.repo;


import com.kwiatowe_imperium.api.models.Category;
import com.kwiatowe_imperium.api.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameEnIgnoreCase(String nameEn);
    Category findByNamePlIgnoreCase(String namePl);


    @Query("SELECT c FROM Category c WHERE c.isVisible = true")
    List<Category> findAllByVisibleIsTrue();

}

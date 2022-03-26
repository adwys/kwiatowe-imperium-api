package com.kwiatowe_imperium.api.repo;


import com.kwiatowe_imperium.api.models.Category;
import com.kwiatowe_imperium.api.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameEnIgnoreCase(String nameEn);
    Category findByNamePlIgnoreCase(String namePl);
}

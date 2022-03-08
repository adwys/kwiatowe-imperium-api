package com.kwiatowe_imperium.api.repo;

import com.kwiatowe_imperium.api.models.Image;
import com.kwiatowe_imperium.api.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
}

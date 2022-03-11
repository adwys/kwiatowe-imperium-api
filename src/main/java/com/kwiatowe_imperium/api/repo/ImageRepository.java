package com.kwiatowe_imperium.api.repo;

import com.kwiatowe_imperium.api.models.Image;
import com.kwiatowe_imperium.api.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

//    @Query("SELECT * FROM Image i JOIN i.product p")
//    public String getInfo();

    public List<Image> findByProductId(Long product_id);
}

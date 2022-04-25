package com.kwiatowe_imperium.api.repo;

import com.kwiatowe_imperium.api.models.Category;
import com.kwiatowe_imperium.api.models.Product;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, PagingAndSortingRepository<Product, Long> {

//    @Query(value ="SELECT p.id,p.name FROM Product p",nativeQuery = true)
    List<Product> findAll();
    Product findByNameEnIgnoreCase(String nameEn);
    Product findByNamePlIgnoreCase(String namePl);

//    @Query("SELECT * from Product where Product.categories =")
//    List<Product> findProductsBy(int category);

    @Query("SELECT COUNT(p) FROM Product p")
    long count();

    @Override
    Page<Product> findAll(Pageable pageable);
}

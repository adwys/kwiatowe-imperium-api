package com.kwiatowe_imperium.api.repo;

import com.kwiatowe_imperium.api.models.Cart;
import com.kwiatowe_imperium.api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByOrderedTrue();
}

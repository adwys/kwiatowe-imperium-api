package com.kwiatowe_imperium.api.repo;


import com.kwiatowe_imperium.api.models.CartToFinalize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartToFinalizeRepository extends JpaRepository<CartToFinalize, Long> {
}

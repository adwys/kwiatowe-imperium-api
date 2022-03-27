package com.kwiatowe_imperium.api.repo;

import com.kwiatowe_imperium.api.models.Category;
import com.kwiatowe_imperium.api.models.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {
}

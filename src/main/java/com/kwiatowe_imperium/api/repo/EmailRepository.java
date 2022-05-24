package com.kwiatowe_imperium.api.repo;

import com.kwiatowe_imperium.api.models.Email;
import com.kwiatowe_imperium.api.models.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    @Query("SELECT e FROM Email e WHERE e.SendTo =?1")
    List<Email> findAllBySendTo(String SendTo);
}

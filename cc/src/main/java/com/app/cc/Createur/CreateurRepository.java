package com.app.cc.Createur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreateurRepository extends JpaRepository<Createur,Long> {
    @Query("SELECT c FROM Createur c WHERE c.role = 'CREATOR'")
    List<Createur> findAllCreators();
}

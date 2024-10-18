package com.app.cc.Createur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateurRepository extends JpaRepository<Createur,Long> {
}

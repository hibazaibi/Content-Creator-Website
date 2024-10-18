package com.app.cc.evaluation;

import com.app.cc.Createur.Createur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
    List<Evaluation> findByCreateur(Createur createur);
}

package com.app.cc.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
    List<Evaluation> findByOffreIdOffre(Long idOffre);}

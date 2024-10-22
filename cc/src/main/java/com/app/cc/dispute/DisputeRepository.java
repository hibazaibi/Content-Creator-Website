package com.app.cc.dispute;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long> {

}

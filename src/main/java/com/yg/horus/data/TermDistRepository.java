package com.yg.horus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermDistRepository extends JpaRepository<TermDist, Long> {
    @Query("SELECT t FROM TermDist t WHERE t.baseTerm = :baseTerm")
    List<TermDist> findLatestTerm(@Param("baseTerm") String baseTerm);
}

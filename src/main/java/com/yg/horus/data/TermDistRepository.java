package com.yg.horus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermDistRepository extends JpaRepository<TermDist, Long> {

    @Query("SELECT t FROM TermDist t WHERE t.baseTerm = :baseTerm AND t.grpTs = :grpTs")
    List<TermDist> findGrpTsTermVectors(@Param("baseTerm") String baseTerm,
                                  @Param("grpTs") long grpTs);

    @Query("SELECT MAX(t.grpTs) FROM TermDist t ")
    long getLatestGrpId();


//    @Query("select td from TermDist td where td.baseTerm = :baseTerm " +
//            "AND td.grpTs = (select max(td2.grpTs) from TermDist td2) ")
//    List<TermDist> findLatestTermVectors(@Param("baseTerm") String baseTerm) ;
//
//    @Query("select td.baseTerm from TermDist td where " +
//            "td.grpTs = (select max(td2.grpTs) from TermDist td2) group by td.baseTerm")
//    List<String> getLatestValidTopics();


    @Query("select td from TermDist td where td.baseTerm = :baseTerm " +
            "AND td.grpTs = :grpTs ")
    List<TermDist> findLatestTermVectors(@Param("baseTerm") String baseTerm, @Param("grpTs") long grpTs) ;

    @Query("select td.baseTerm from TermDist td where " +
            "td.grpTs = :grpTs group by td.baseTerm")
    List<String> getLatestValidTopics(@Param("grpTs") long grpTs);


}

package com.yg.horus.data;

import com.yg.horus.doc.DailyInvestDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.util.List;

/**
 * Created by a1000074 on 13/05/2021.
 */
@Repository
public interface KospiRepository extends JpaRepository<DailyInvestDoc, String> {
    DailyInvestDoc findOneByTargetDt(String targetDt);
    List<DailyInvestDoc> findAll() ;
    @Query("SELECT u FROM DailyInvestDoc u WHERE u.upDownPer > :gtValue AND u.upDownPer < :ltValue")
    List<DailyInvestDoc> getRangedIndexes(@Param("gtValue") float gtValue, @Param("ltValue") float ltValue);

}

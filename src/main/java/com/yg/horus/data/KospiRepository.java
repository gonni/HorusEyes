package com.yg.horus.data;

import com.yg.horus.doc.DailyInvestDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by a1000074 on 13/05/2021.
 */
@Repository
public interface KospiRepository extends JpaRepository<DailyInvestDoc, String> {
    DailyInvestDoc findOneByTargetDt(String targetDt);
    List<DailyInvestDoc> findAll() ;
}

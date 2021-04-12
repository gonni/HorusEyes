package com.yg.horus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by a1000074 on 15/03/2021.
 */
@Repository
public interface CrawlRepository  extends JpaRepository<CrawlUnit, Long> {
    public List<CrawlUnit> findByStatus(String status) ;
    public List<CrawlUnit> findByAnchorText(String anchorText) ;

}
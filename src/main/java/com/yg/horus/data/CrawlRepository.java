package com.yg.horus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by a1000074 on 15/03/2021.
 */
@Repository
public interface CrawlRepository  extends JpaRepository<CrawlUnitVo, Long> {
    public List<CrawlUnitVo> findByStatus(String status) ;
    public List<CrawlUnitVo> findByAnchorText(String anchorText) ;

}
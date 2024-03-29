package com.yg.horus.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by a1000074 on 15/03/2021.
 */
@Repository
public interface CrawlRepository  extends JpaRepository<CrawlUnit, Long> {
    List<CrawlUnit> findByStatus(String status) ;
    List<CrawlUnit> findByAnchorText(String anchorText) ;
    CrawlUnit findOneByUrl(String url) ;

    List<CrawlUnit> findByStatusOrderByCrawlNoDesc(CrawlStatus crawlStatus, Pageable pageable) ;
    List<CrawlUnit> findByTopSeedsSeedNoOrderByCrawlNoDesc(long seedNo, Pageable pageable) ;
    List<CrawlUnit> findByStatusAndTopSeedsSeedNoOrderByCrawlNoDesc(CrawlStatus crawlStatus, long seedNo, Pageable pageable) ;
    List<CrawlUnit> findByStatusAndTopSeedsSeedNoOrderByCrawlNoAsc(CrawlStatus crawlStatus, long seedNo, Pageable pageable) ;

    @Query("SELECT u FROM CrawlUnit u WHERE u.pageDate >= :startDate AND u.pageDate < :endDate")
    List<CrawlUnit> getDateRangedUnits(@Param("startDate") String startDate, @Param("endDate") String endDate) ;
    List<CrawlUnit> findByPageDateStartsWith(String targetDt);

    @Query("SELECT COUNT(u) FROM CrawlUnit u WHERE u.topSeeds.seedNo =:seedNo AND u.status = 'IURL'")
    long getCountOfInitUnits(@Param("seedNo") long seedNo) ;
}
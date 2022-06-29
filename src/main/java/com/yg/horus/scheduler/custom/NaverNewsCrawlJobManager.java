package com.yg.horus.scheduler.custom;

import com.yg.horus.crawl.custom.NaverStockIndexCrawler;
import com.yg.horus.data.KospiRepository;
import com.yg.horus.scheduler.ranged.JobBuilder;
import com.yg.horus.scheduler.ranged.JobScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NaverNewsCrawlJobManager {
    @Autowired
    private JobBuilder jobBuilder = null ;
    @Autowired
    private JobScheduler jobScheduler = null ;
    @Autowired
    private NaverStockIndexCrawler naverStockIndexCrawler = null ;
    @Autowired
    private KospiRepository kospiRepository = null ;

    public NaverNewsCrawlJobManager() {
        ;
    }

}

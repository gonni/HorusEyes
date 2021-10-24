package com.yg.horus.scheduler.listcrawl;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.scheduler.Job;
import com.yg.horus.scheduler.JobStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CommonListCrawlJob implements Job<List<CrawlDataUnit>> {
    private PageIndexListUrlCrawlJoblet crawlJoblet = null ;
    private List<CrawlDataUnit> latestCrawled = null ;

    //Data Range, SeedIndex
    public CommonListCrawlJob() {
        ;
    }

    @Override
    public List<CrawlDataUnit> start() {



        return null;
    }

    @Override
    public JobStatus getStatus() {
        return null;
    }
}

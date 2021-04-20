package com.yg.horus.scheduler;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.HttpResourceCrawler;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Observable;

/**
 * Created by jeff on 21. 4. 18.
 */
@Slf4j
public class ListUrlCrawllJob extends Observable implements Job {

    private JobStatus jobStatus = JobStatus.INIT;
    CrawlRepository crawlRepository = null ;
    private String seedUrl = null ;
    String crawlUrlRegxPattern = null ;
    String crawlUrlAreaQuery = null ;

    @Builder
    public ListUrlCrawllJob(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    @Override
    public void start() {
        this.jobStatus = JobStatus.PROCESSING ;
        HttpResourceCrawler crawler = new HttpResourceCrawler();

        List<CrawlDataUnit> matchedLinks = crawler.getMatchedLinks(this.seedUrl, this.crawlUrlRegxPattern, this.crawlUrlAreaQuery);
        int cntNew = 0;
        for(CrawlDataUnit link : matchedLinks) {
            CrawlUnit crawlUnit = this.crawlRepository.findOneByUrl(link.getUrl());

            if(crawlUnit == null) {
                crawlUnit = CrawlUnit.builder()
                        .url(link.getUrl())
                        .anchorText(link.getAnchorText())
                        .status(CrawlStatus.IURL)
                        .build();

                this.crawlRepository.save(crawlUnit);
                cntNew++;
            } else {
                log.info("Dupplicated : {}", link);
            }
        }

        log.info("Added News Links : {}/{}", cntNew, matchedLinks.size());
        this.jobStatus = JobStatus.COMPLETED ;
    }

    @Override
    public JobStatus getStatus() {
        return this.jobStatus ;
    }

    public static void main(String ... v) {
        System.out.println("Active System .. ");

    }

}

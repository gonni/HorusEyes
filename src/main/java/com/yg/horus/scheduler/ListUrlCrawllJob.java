package com.yg.horus.scheduler;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.HttpResourceCrawler;
import com.yg.horus.data.CrawlRepository;
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

        //TODO store data(need to check dupplicated
        matchedLinks.forEach(link -> {

            List<CrawlUnit> crawlUnits = this.crawlRepository.findByUrl(link.getUrl());
            if(crawlUnits != null && crawlUnits.size() > 0) {
                log.info("Duplicated : {}", link);
            } else {
                this.crawlRepository.save(crawlUnits.get(0));
            }
        });
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

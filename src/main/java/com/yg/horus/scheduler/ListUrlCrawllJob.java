package com.yg.horus.scheduler;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.HttpResourceCrawler;
import com.yg.horus.data.CrawlRepository;
import lombok.Builder;

import java.util.List;
import java.util.Observable;

/**
 * Created by jeff on 21. 4. 18.
 */
public class ListUrlCrawllJob extends Observable implements Job {

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
        HttpResourceCrawler crawler = new HttpResourceCrawler();

        List<CrawlDataUnit> matchedLinks = crawler.getMatchedLinks(this.seedUrl, this.crawlUrlRegxPattern, this.crawlUrlAreaQuery);

        //TODO store data(need to check dupplicated
        matchedLinks.forEach(link -> {

            ;

        });

    }

    @Override
    public JobStatus getStatus() {

        return null;
    }

    public static void main(String ... v) {
        System.out.println("Active System .. ");

    }

}

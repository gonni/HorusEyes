package com.yg.horus.scheduler;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.ListPageCrawler;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.data.TopSeeds;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Observable;

/**
 * Created by jeff on 21. 4. 18.
 */
@Slf4j
public class ListUrlCrawllJob extends Observable implements Job<List<CrawlDataUnit>> {
    private JobStatus jobStatus = JobStatus.INIT;
    CrawlRepository crawlRepository = null ;
    String crawlUrlRegxPattern = null ;
    String crawlUrlAreaQuery = null ;
    private TopSeeds topSeeds = null ;

    public ListUrlCrawllJob(TopSeeds topSeeds) {
        this.topSeeds = topSeeds ;
    }

    public ListUrlCrawllJob(String targetUrl, long referSeedNo) {
        this.topSeeds = TopSeeds.builder().urlPattern(targetUrl).build();
        this.topSeeds.setSeedNo(referSeedNo);
    }

    @Override
    public List<CrawlDataUnit> start() {
        this.jobStatus = JobStatus.PROCESSING ;
        ListPageCrawler crawler = new ListPageCrawler();

        List<CrawlDataUnit> matchedLinks = crawler.getMatchedLinks(this.topSeeds.getUrlPattern(), this.crawlUrlRegxPattern, this.crawlUrlAreaQuery);
        int cntNew = 0;
        for(CrawlDataUnit link : matchedLinks) {
            CrawlUnit crawlUnit = this.crawlRepository.findOneByUrl(link.getUrl());

            if(crawlUnit == null) {
                crawlUnit = CrawlUnit.builder()
                        .url(link.getUrl())
                        .anchorText(link.getAnchorText())
                        .status(CrawlStatus.IURL)
                        .build();

//                crawlUnit.setSeedNo(this.seedNo);
                if(link.getAnchorType().equals(CrawlDataUnit.AnchorType.IMG)) {
                    crawlUnit.setAnchorImg(link.getAnchorText());
                    crawlUnit.setAnchorText(null);
                }
                cntNew++;
            } else {
                log.info("Dupplicated : {}", link);

                if(link.getAnchorType().equals(CrawlDataUnit.AnchorType.IMG)) {
                    crawlUnit.setAnchorImg(link.getAnchorText());
                } else if(link.getAnchorType().equals(CrawlDataUnit.AnchorType.TEXT)) {
                    crawlUnit.setAnchorText(link.getAnchorText());
                }
            }
            crawlUnit.setTopSeeds(this.topSeeds);

            this.crawlRepository.save(crawlUnit);
        }


        log.info("Added News Links : {}/{}", cntNew, matchedLinks.size());
        if(cntNew > 0) {
            this.jobStatus = JobStatus.COMPLETED;
        } else {
            this.jobStatus = JobStatus.COMPLETED_NORESULT;
        }

        return matchedLinks ;
    }

    @Override
    public JobStatus getStatus() {
        return this.jobStatus ;
    }

    public static void main(String ... v) {
        System.out.println("Active System .. ");

    }

}

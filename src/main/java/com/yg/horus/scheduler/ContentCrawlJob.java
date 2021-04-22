package com.yg.horus.scheduler;

import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.crawl.PageWrapper;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.doc.ContentPageDoc;
import lombok.extern.slf4j.Slf4j;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Observable;

/**
 * Created by jeff on 21. 4. 18.
 */
@Slf4j
public class ContentCrawlJob extends Observable implements Job {
    private JobStatus jobStatus = JobStatus.INIT ;
    private CrawlRepository crawlRepository = null ;
//    private String url ;
    private CrawlUnit crawlUnit = null ;
    private ContentsPageWrappingRule wrapRule ;

//    public ContentCrawlJob(String url, ContentsPageWrappingRule wrapRule, CrawlRepository cr) {
//        this.url = url ;
//        this.wrapRule = wrapRule ;
//        this.crawlRepository = cr ;
//    }

    public ContentCrawlJob(CrawlUnit cu, ContentsPageWrappingRule wrapRule, CrawlRepository cr) {
        this.crawlUnit = cu ;
        this.wrapRule = wrapRule ;
        this.crawlRepository = cr ;
    }

    @Override
    public void start() {
        if(this.crawlUnit == null) {
            log.info("Invalid Unit to crawl contents ..");
            return ;
        }

        PageWrapper pageWrapper = new PageWrapper();
        ContentPageDoc contentPageDoc = pageWrapper.getContentPageDoc(this.crawlUnit.getUrl(), wrapRule);
        if(contentPageDoc != null) {
            this.crawlUnit.setPageTitle(contentPageDoc.getTitleOnContent());
            this.crawlUnit.setPageText(contentPageDoc.getContent());
            this.crawlUnit.setPageDate(contentPageDoc.getDateOnContent());
            this.crawlUnit.setStatus(CrawlStatus.SUCC);
        } else {
            this.crawlUnit.setStatus(CrawlStatus.FAIL);
        }
        this.crawlUnit.setUpdDate(LocalDateTime.now());

        this.crawlRepository.save(this.crawlUnit);
    }

    @Override
    public JobStatus getStatus() {
        return this.jobStatus ;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", this.crawlUnit, this.wrapRule);
    }
}

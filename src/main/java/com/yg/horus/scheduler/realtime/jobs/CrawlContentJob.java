package com.yg.horus.scheduler.realtime.jobs;

import com.yg.horus.crawl.ContentsPageCrawler;
import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.doc.ContentPageDoc;
import com.yg.horus.scheduler.realtime.Job;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class CrawlContentJob implements Job<CrawlUnit> {
    private CrawlRepository crawlRepository = null ;
    private CrawlUnit crawlUnit = null ;
    private ContentsPageWrappingRule wrapRule ;

    public CrawlContentJob(CrawlUnit cu, ContentsPageWrappingRule wrapRule, CrawlRepository cr) {
        this.crawlUnit = cu ;
        this.wrapRule = wrapRule ;
        this.crawlRepository = cr ;
    }

    @Override
    public void start() {
        if(this.crawlUnit == null) {
            log.info("Invalid Unit to crawl contents ..");
        }

        ContentsPageCrawler contentsPageCrawler = new ContentsPageCrawler();
        ContentPageDoc contentPageDoc = contentsPageCrawler.getContentPageDoc(this.crawlUnit.getUrl(), wrapRule);
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
    public CrawlUnit getCrawled() {
        return this.crawlUnit;
    }
}

package com.yg.horus.scheduler;

import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.crawl.PageWrapper;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.doc.ContentPageDoc;
import lombok.extern.slf4j.Slf4j;

import java.util.Observable;

/**
 * Created by jeff on 21. 4. 18.
 */
@Slf4j
public class ContentCrawlJob extends Observable implements Job {

    private CrawlRepository crawlRepository = null ;
    private String url ;
    private ContentsPageWrappingRule wrapRule ;

    public ContentCrawlJob(String url, ContentsPageWrappingRule wrapRule, CrawlRepository cr) {
        this.url = url ;
        this.wrapRule = wrapRule ;
        this.crawlRepository = cr ;
    }

    @Override
    public void start() {
        PageWrapper pageWrapper = new PageWrapper();

        ContentPageDoc contentPageDoc = pageWrapper.getContentPageDoc(this.url, wrapRule);

        CrawlUnit crawlUnit = this.crawlRepository.findOneByUrl(this.url);

        //TODO need to add pageTitle
    }

    @Override
    public JobStatus getStatus() {
        return null ;
    }
}

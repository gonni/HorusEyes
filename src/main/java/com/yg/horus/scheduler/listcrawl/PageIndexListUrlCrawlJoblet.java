package com.yg.horus.scheduler.listcrawl;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.ListPageCrawler;
import com.yg.horus.scheduler.Joblet;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PageIndexListUrlCrawlJoblet implements Joblet<CrawlDataUnit> {
    private static final int PAGE_INDEX_LIMIT = 55 ;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;

    private String seedUrlPattern = null ;
    private String startDateString = null ;
    private String endDateString = null ;

    private String urlGrabPattern = "^(https:\\/\\/news.naver.com\\/main\\/read.naver\\?).*$";
    private String groupHeadPath = "ul.type02";

    public PageIndexListUrlCrawlJoblet(String seedUrlPattern, String startDateString, String endDateString) {
        this.seedUrlPattern = seedUrlPattern ;
        this.startDateString = startDateString ;
        this.endDateString = endDateString ;
        ;
    }

    public List<CrawlDataUnit> crawl(String date, int pageIndex) {
        String targetUrl = String.format(this.seedUrlPattern, date, pageIndex) ;
        List<CrawlDataUnit> crawlDataUnits = this.processingCrawlList(targetUrl, urlGrabPattern, groupHeadPath);

//        if(latestCrawled!= null && latestCrawled.containsAll(crawlDataUnits)) {
//            // break condition
//            log.info("====> Detected Same Array page : {} = {}", pageIndex, latestCrawled.size());
//        }

        return crawlDataUnits ;
    }

    private List<CrawlDataUnit> processingCrawlList(String targetUrl, String grabUrlPattern, String groupHeadPath) {
        ListPageCrawler listCrawler = new ListPageCrawler();
        List<CrawlDataUnit> matchedLinks = listCrawler.getMatchedLinks(targetUrl, grabUrlPattern, groupHeadPath);

        return matchedLinks ;
    }

    @Override
    public CrawlDataUnit start() {
        return null;
    }

    @Override
    public void onCompleted(CrawlDataUnit crawlDataUnit) {

    }
}

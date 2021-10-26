package com.yg.horus.scheduler.listcrawl;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.ListPageCrawler;
import com.yg.horus.scheduler.AbstrackJoblet;
import com.yg.horus.scheduler.Joblet;
import com.yg.horus.scheduler.JobletEventListener;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PageIndexListUrlCrawlJoblet extends AbstrackJoblet<List<CrawlDataUnit>> implements Joblet<List<CrawlDataUnit>> {
    private static final int PAGE_INDEX_LIMIT = 55 ;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;

    private String targetUrl = null ;


    private String urlGrabPattern = "^(https:\\/\\/news.naver.com\\/main\\/read.naver\\?).*$";
    private String groupHeadPath = "ul.type02";

    public PageIndexListUrlCrawlJoblet(String targetUrl, String grabUrlPattern, String grpQry) {
        this.targetUrl = targetUrl ;
        this.urlGrabPattern = grabUrlPattern ;
        this.groupHeadPath = grpQry ;
        ;
    }

    public List<CrawlDataUnit> crawl(String date, int pageIndex) {
        String targetUrl = String.format(this.targetUrl, date, pageIndex) ;
        List<CrawlDataUnit> crawlDataUnits = this.processingCrawlList();
        return crawlDataUnits ;
    }

    private List<CrawlDataUnit> processingCrawlList() {
        ListPageCrawler listCrawler = new ListPageCrawler();
        List<CrawlDataUnit> matchedLinks = listCrawler.getMatchedLinks(this.targetUrl, this.urlGrabPattern, this.groupHeadPath);

        return matchedLinks ;
    }

    @Override
    public List<CrawlDataUnit> start() {
        System.out.println("joblet started");
        List<CrawlDataUnit> crawlDataUnits = this.processingCrawlList();

        System.out.println("joblet started .. " + crawlDataUnits);
        if(crawlDataUnits != null) {
            super.updateAll(JOBLET_STATUS.COMPLETED, crawlDataUnits);
        } else {
            super.updateAll(JOBLET_STATUS.NULL, null);
        }

        return crawlDataUnits;
    }

    @Override
    public JOBLET_STATUS getStatus() {
        return null;
    }

    public static void main(String ... v) {
//        String seedPattern = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=263&sid1=101&mid=sec&listType=title&date=%s&page=%s";
        String targetUrl = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=263&sid1=101&mid=sec&listType=title&date=20211023&page=3";
        String grabUrlPattern = "^(https:\\/\\/news.naver.com\\/main\\/read.naver\\?).*$";
        String group = "ul.type02";

        PageIndexListUrlCrawlJoblet test = new PageIndexListUrlCrawlJoblet(targetUrl, grabUrlPattern, group) ;
        test.crawl("20211020", 1).forEach(System.out::println);
    }

}

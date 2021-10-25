package com.yg.horus.scheduler.listcrawl;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.ListPageCrawler;
import com.yg.horus.scheduler.Job;
import com.yg.horus.scheduler.JobStatus;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by a1000074 on 22/10/2021.
 */
@Slf4j
public class RangedListUrlCrawlJoblet implements Job<List<CrawlDataUnit>> {
    private static final int PAGE_INDEX_LIMIT = 55 ;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;

    private String seedUrlPattern = null ;
    private String startDateString = null ;
    private String endDateString = null ;

    private String urlGrabPattern = "^(https:\\/\\/news.naver.com\\/main\\/read.naver\\?).*$";
    private String groupHeadPath = "ul.type02";

    List<CrawlDataUnit> latestCrawled = null ;
    private boolean isEnd = false ;

    public RangedListUrlCrawlJoblet(String seedUrlPattern, String startDateString, String endDateString) {
        this.seedUrlPattern = seedUrlPattern ;
        this.startDateString = startDateString ;
        this.endDateString = endDateString ;
        ;
    }

    @Override
    public List<CrawlDataUnit> start() {
        List<CrawlDataUnit> latestCrawled = null ;

        String cursorDateString = startDateString ;

        String targetUrl = null ;
        while(endDateString.compareTo(cursorDateString) >= 0) {

            for(int i=1;i<PAGE_INDEX_LIMIT;i++) {
                log.info("TURN:{}, Check Page Num : {}", cursorDateString, i);
                targetUrl = String.format(this.seedUrlPattern, cursorDateString, i) ;
                log.info("Target ==> {}", targetUrl);
                List<CrawlDataUnit> crawlDataUnits = this.processingCrawlList(targetUrl,
                        urlGrabPattern, groupHeadPath);
                log.info("Grabbed : {} turn no #{}", crawlDataUnits.size(), i);
//                crawlDataUnits.forEach(System.out::println);


                if(latestCrawled != null)
                    log.info("latest : current = {} : {}", latestCrawled.size(), crawlDataUnits.size());

                // TODO define method wheather end condition is not
                if(latestCrawled!= null && latestCrawled.containsAll(crawlDataUnits)) {
                    // break condition
                    log.info("====> Detected Same Array page : {} = {}", i, latestCrawled.size());
                    break ;
                }

                latestCrawled = crawlDataUnits;

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("ToCursor --> " + cursorDateString);
            cursorDateString = getNextday(cursorDateString);
            System.out.println("Cursor --> " + cursorDateString);
        }


        return null;
    }

    private String getNextday(String yyyymmdd) {
        try {
            Date date = sdf.parse(yyyymmdd);

            Calendar day = Calendar.getInstance();
            day.setTime(date);
            day.add(Calendar.DATE, 1);

            return this.sdf.format(day.getTime()) ;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null ;
    }


    private List<CrawlDataUnit> processingCrawlList(String targetUrl, String grabUrlPattern, String groupHeadPath) {

        ListPageCrawler listCrawler = new ListPageCrawler();
        List<CrawlDataUnit> matchedLinks = listCrawler.getMatchedLinks(targetUrl, grabUrlPattern, groupHeadPath);

//        log.info("Grabbed : %s", matchedLinks.size());
        return matchedLinks ;
    }

    private List<CrawlDataUnit> processingCrawlList(String targetUrl, Map<String, String> kvp) {

        return null ;
    }

    @Override
    public JobStatus getStatus() {
        return JobStatus.NOT_AVAILABLE ;
    }

    public static void main(String ... v) {
        System.out.println("Hella");

        RangedListUrlCrawlJoblet test = new RangedListUrlCrawlJoblet(
                "https://news.naver.com/main/list.naver?mode=LS2D&sid2=263&sid1=101&mid=sec&listType=title&date=%s&page=%s",
                "20211020",
                "20211020"
        );

        test.start();

//        String seedUrl = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=263&sid1=101&mid=sec&listType=title&date=20211020&page=3";
//        String regexFilter = "^(https:\\/\\/news.naver.com\\/main\\/read.naver\\?).*$";
//        String groupHeadPath = "ul.type02";
//        List<CrawlDataUnit> matchedLinks = test.processingCrawlList(seedUrl, regexFilter, groupHeadPath) ;
//
//        System.out.println("====> ReX Filtered :" + matchedLinks.size());
//        matchedLinks.stream().forEach(System.out::println);


    }
}

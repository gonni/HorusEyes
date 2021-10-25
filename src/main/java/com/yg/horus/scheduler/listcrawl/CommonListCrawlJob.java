package com.yg.horus.scheduler.listcrawl;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.TopSeeds;
import com.yg.horus.scheduler.*;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class CommonListCrawlJob implements Job<List<CrawlDataUnit>>, JobletEventListener<List<CrawlDataUnit>> {
    private static final int PAGE_INDEX_LIMIT = 55 ;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;

    private JobStatus jobStatus = JobStatus.INIT ;
    private PageIndexListUrlCrawlJoblet crawlJoblet = null ;
    private List<CrawlDataUnit> latestCrawled = null ;

    protected JobletProcessor jobletProcessor = null ;
    protected CrawlRepository crawlRepository = null ;

    String crawlUrlRegxPattern = null ;
    String crawlUrlAreaQuery = null ;

    private String currentDateString = null ;
    private String startDateString = null ;
    private String endDateString = null ;

    private int pageIndex = 0;


    private TopSeeds topSeeds = null ;

    //Data Range, SeedIndex
    public CommonListCrawlJob(long seedId,
                              String seedUrlPattern,
                              String grabUrlPattern,
                              String areaGrpupQuery,
                              String startDate,
                              String endDate,
                              int startPageIndex) {
        this.currentDateString = startDate ;
        this.startDateString = startDate ;
        this.endDateString = endDate ;

        this.topSeeds = TopSeeds.builder().urlPattern(seedUrlPattern).build();
        this.topSeeds.setSeedNo(seedId);

        this.crawlUrlAreaQuery = areaGrpupQuery ;
        this.crawlUrlRegxPattern = grabUrlPattern ;

        if(startPageIndex > 0) this.pageIndex = startPageIndex;
    }

    private PageIndexListUrlCrawlJoblet createNewJoblet() {

        PageIndexListUrlCrawlJoblet joblet = new PageIndexListUrlCrawlJoblet(this.topSeeds.getUrlPattern(),
                this.crawlUrlRegxPattern, this.crawlUrlAreaQuery) ;
        joblet.addActionListener(this);

        return joblet ;
    }

    @Override
    public List<CrawlDataUnit> start() {

        this.processNext();

        return null;
    }


    private boolean processNext() {
        if(this.endDateString.compareTo(this.startDateString) >= 0) {
            if(!this.jobStatus.equals(JobStatus.COMPLETED) && this.pageIndex < PAGE_INDEX_LIMIT) {

                String targetUrl = String.format(this.topSeeds.getUrlPattern(), this.currentDateString, this.pageIndex) ;
                log.info("Crawl Try : {}", targetUrl);
                this.crawlJoblet = new PageIndexListUrlCrawlJoblet(targetUrl, this.crawlUrlRegxPattern, this.crawlUrlAreaQuery);

                this.jobletProcessor.schedule(crawlJoblet);

                this.pageIndex ++;
                this.currentDateString = this.getNextday(this.currentDateString);
                return true ;

            } else {
                //TODO ...
                log.info("Detected Job Completed page index :{} at {}", this.pageIndex, this.currentDateString);
            }
        }

        return false ;
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

    @Override
    public JobStatus getStatus() {
        return null;
    }

    @Override
    public void eventOccurred(Joblet joblet, Joblet.JOBLET_STATUS status, List<CrawlDataUnit> result) {
        if(status.equals(Joblet.JOBLET_STATUS.COMPLETED)
                || status.equals(Joblet.JOBLET_STATUS.FAILED)) {
            if(latestCrawled!= null && latestCrawled.containsAll(result)) {
                // break condition
                log.info("====> Detected Same Array page : {} = {}", latestCrawled.size());
                this.jobStatus = JobStatus.COMPLETED ;
            }
        } else if(status.equals(Joblet.JOBLET_STATUS.FAILED)) {
            this.jobStatus = JobStatus.FAILED ;
        }
    }

    public static void main(String ... v) {
        System.out.println("Hella ..");
    }

}

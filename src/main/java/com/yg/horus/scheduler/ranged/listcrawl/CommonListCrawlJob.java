package com.yg.horus.scheduler.ranged.listcrawl;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.data.TopSeeds;
import com.yg.horus.scheduler.ranged.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class CommonListCrawlJob implements Job<List<CrawlDataUnit>>, JobletEventListener<List<CrawlDataUnit>> {
    private static final int PAGE_INDEX_LIMIT = 200 ;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;

    private JobStatus jobStatus = JobStatus.INIT ;
    private PageIndexListUrlCrawlJoblet crawlJoblet = null ;
    private List<CrawlDataUnit> latestCrawled = null ;

    @Setter
    private JobletProcessor jobletProcessor = null ;
    @Setter
    private CrawlRepository crawlRepository = null ;

    private String crawlUrlRegxPattern = null ;
    private String crawlUrlAreaQuery = null ;
    private String currentDateString = null ;
//    private String startDateString = null ;
    private String endDateString = null ;

    private int pageIndex = 0;
    private TopSeeds topSeeds = null ;

    private long latestEv = System.currentTimeMillis() ;

    //Data Range, SeedIndex
    public CommonListCrawlJob(long seedId,
                              String seedUrlPattern,
                              String grabUrlPattern,
                              String areaGrpupQuery,
                              String startDate,
                              String endDate,
                              int startPageIndex) {
        this.currentDateString = startDate ;
//        this.startDateString = startDate ;
        this.endDateString = endDate ;

        this.topSeeds = TopSeeds.builder().urlPattern(seedUrlPattern).build();
        this.topSeeds.setSeedNo(seedId);

        this.crawlUrlAreaQuery = areaGrpupQuery ;
        this.crawlUrlRegxPattern = grabUrlPattern ;

        if(startPageIndex > 0) this.pageIndex = startPageIndex;
    }

    public static void main(String ... v) {
        System.out.println("Hella ..");

        JobletProcessor jp = new JobletProcessor("NaverEcoNewsWorker");
        jp.startWorker();

        CommonListCrawlJob testJob = new CommonListCrawlJob(
                28,
                "https://news.naver.com/main/list.naver?mode=LS2D&sid2=263&sid1=101&mid=sec&listType=title&date=%s&page=%s",
                "^(https:\\/\\/news.naver.com\\/main\\/read.naver\\?).*$",
                "ul.type02",
                "20211024",
                "20211029",
                1);
        testJob.jobletProcessor = jp ;

        testJob.start() ;
    }

    private PageIndexListUrlCrawlJoblet createNewJoblet() {

        PageIndexListUrlCrawlJoblet joblet = new PageIndexListUrlCrawlJoblet(this.topSeeds.getUrlPattern(),
                this.crawlUrlRegxPattern, this.crawlUrlAreaQuery) ;
        joblet.addActionListener(this);

        return joblet ;
    }

    @Override
    public List<CrawlDataUnit> start() {
        //TODO need add logic : monitor status and latest event time gab

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                processNext();
//            }
//        }).start();

        processNext();

        // Thread for blocking monitor
        new Thread(()->{
            int cntBlock = 0;

            while(!this.jobStatus.equals(JobStatus.COMPLETED)) {

                if((System.currentTimeMillis() - latestEv) > 10000L ){
                    log.info("[Critical] Detected Blocked Job Status ..");
                    processNext();

                    if(cntBlock++ > 5) {
                        break ;
                    }
                }

                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            log.info("Detected ranged job completed ..");
        }).start();



         // Async
//

        return null;
    }


    private boolean processNext() {
        log.info("process next {} => {}", currentDateString, pageIndex);
        if(!this.jobStatus.equals(JobStatus.COMPLETED) && this.endDateString.compareTo(this.currentDateString) >= 0) {
            if(!this.jobStatus.equals(JobStatus.COMPLETED) && this.pageIndex < PAGE_INDEX_LIMIT) {
                System.out.println("detected");

                String targetUrl = String.format(this.topSeeds.getUrlPattern(), this.currentDateString, this.pageIndex) ;
                log.info("Crawl Try : {}", targetUrl);
                this.crawlJoblet = new PageIndexListUrlCrawlJoblet(targetUrl, this.crawlUrlRegxPattern, this.crawlUrlAreaQuery);
                this.crawlJoblet.addActionListener(this);

                this.jobletProcessor.schedule(crawlJoblet);

                this.pageIndex ++;
//                this.currentDateString = this.getNextday(this.currentDateString);
                return true ;

            } else {
                //TODO ...
                log.info("Detected Job Completed page index :{} at {}", this.pageIndex, this.currentDateString);
                this.pageIndex = 0;
                this.currentDateString = this.getNextday(this.currentDateString);
                processNext();
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
    public long getSeedId() {
        return this.topSeeds.getSeedNo();
    }

    @Override
    public void eventOccurred(Joblet joblet, Joblet.JOBLET_STATUS status, List<CrawlDataUnit> result) {
        System.out.println("ev " + status + " -> " + result);
        this.latestEv = System.currentTimeMillis() ;
        if(status.equals(Joblet.JOBLET_STATUS.COMPLETED)
                || status.equals(Joblet.JOBLET_STATUS.NULL)) {
            if(latestCrawled!= null && latestCrawled.containsAll(result)) {
                // break condition
                log.info("====> Detected Same Array page : {}.{} = {}", this.currentDateString, this.pageIndex, latestCrawled.size());
//                this.jobStatus = JobStatus.COMPLETED ;

                //TODO need
                this.currentDateString = this.getNextday(this.currentDateString);
                this.pageIndex = 1;

                if(this.endDateString.compareTo(this.currentDateString) >= 0) {
                    log.info("Day Changed Detected ..");
                    processNext();
                } else {
                    log.info("Detected Job Completed !! Need to stop ..");
                    this.jobStatus = JobStatus.COMPLETED ;
                }

            } else {
                log.info("=====> Detected Next : {} -> {}", status, result);
                if(!this.storeCrawlData(result)) {
                    log.info("Store data failed ..");
                }
                processNext();
            }
        } else if(status.equals(Joblet.JOBLET_STATUS.FAILED)) {
            this.jobStatus = JobStatus.FAILED ;
        }

        this.latestCrawled = result;
    }

    private boolean storeCrawlData(List<CrawlDataUnit> result) {
        if(this.crawlRepository != null ) {
            result.forEach(link -> {
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
                    // cnt++
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
            });
        }

        return false ;
    }

}

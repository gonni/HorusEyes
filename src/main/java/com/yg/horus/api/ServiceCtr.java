package com.yg.horus.api;

//import com.yg.horus.data.MemberRepository;
import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.scheduler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by a1000074 on 30/12/2020.
 */
@RestController
@Slf4j
public class ServiceCtr {
    @Autowired
    private JobProducer jobProducer = null ;
    @Autowired
    private JobManager jobManager = null ;
    @Autowired
    private JobScheduler jobScheduler = null ;
    @Autowired
    private NaverStockJobManager naverStockJobManager = null ;
    @Value("${horus.engine.version}")
    private String version ;

    @RequestMapping("/system/version")
    public String getVersion() {
        return this.version + " at " + new Date(System.currentTimeMillis()) ;
    }

    @RequestMapping("/system/status")
    public String getSystemStatus() {
        String schedulerMsg = this.jobScheduler.getStatusMessage();
        return "JobScheduler Status :" + schedulerMsg ;
    }

    @RequestMapping("/system/start")
    public String startSystem() {
        log.info("start contents crawl workers ..");
        this.jobProducer.addJobSet(2);
        this.jobProducer.startWorker();

        return "Started Contents Crawler ..";
    }


    @RequestMapping("/crawl/contents")
    public String crawlContents(@RequestParam long seedNo, @RequestParam int maxJobCnt) {
        log.info("Detected unit crawl for #{} limit {}", seedNo, maxJobCnt);
        int scheduled = this.jobProducer.crawlContents(seedNo, maxJobCnt) ;

        return String.format("Job linked with seedNo:%s scheduled : %d", seedNo, scheduled);
    }

    @RequestMapping("/crawl/lsturls")
    public String crawlTargetUrlList(@RequestParam long seedNo) {
        log.info("Detected list crawl for seed #{}", seedNo);
        Job<ListUrlCrawllJob> lstCrawlJob = this.jobManager.createSeedListCrawlJob(seedNo);

        List<CrawlDataUnit> lstCrawlData = (List<CrawlDataUnit>)lstCrawlJob.start();
        lstCrawlData.forEach(du -> {
            log.info("List Crawl: {}", du.toString());
        });

        return String.format("List-crawl Job completed : %d", lstCrawlData.size()) ;
    }

    @RequestMapping("/crawl/naver/stocknews")
    public String crawlNaverUrlsList(@RequestParam long seedNo, @RequestParam String start, @RequestParam String end) {
        if(start.length() == 8 && end.length() == 8) {
            new Thread(() -> {
                this.naverStockJobManager.execSerialJobs(seedNo, start, end);
            }).start();
        } else {
            log.info("Invalid parameters : {} -> {}", start, end);
            return "Invalid params which should be yyyyMMdd format ..";
        }

        return "Naver Thread Started .." ;
    }

//    @Autowired
//    private MemberRepository memberRepository = null;
//
//    public ServiceCtr() {
//        log.info("Init Common Service Controller ..");
//    }
//
//    @RequestMapping("/yg/horus/hell")
//    public @ResponseBody String hell() {
//        log.info("Detencted Hell-Ha ..");
//
//
//        this.memberRepository.findAll().forEach(System.out::println);
//
//
//        return "{\"dt\": "+System.currentTimeMillis()+"}" ;
//    }
}

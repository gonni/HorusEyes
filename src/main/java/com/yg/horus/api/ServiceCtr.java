package com.yg.horus.api;

//import com.yg.horus.data.MemberRepository;
import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.nlp.paragraphvectors.TopicCluster;
import com.yg.horus.nlp.word2vec.Word2vecModeler;
import com.yg.horus.scheduler.custom.NaverStockJobManager;
import com.yg.horus.scheduler.ranged.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private JobBuilder jobBuilder = null ;
    @Autowired
    private JobScheduler jobScheduler = null ;
    @Autowired
    private ManagedJobScheduler managedJobScheduler = null ;
    @Autowired
    private NaverStockJobManager naverStockJobManager = null ;
    @Autowired
    private Word2vecModeler word2vecModeler = null;
    @Autowired
    private TopicCluster topicCluster = null ;

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

    @RequestMapping("/crawl/managed/list")
    public String crawlMananaged() {
        Job<List<CrawlDataUnit>> job = this.managedJobScheduler.createManagedListCrawlJob(
                2L, "20211023", "20211029");
        job.start();

        return "Scheduled managed job ..";
    }


    @RequestMapping("/crawl/contents")
    public String crawlContents(@RequestParam long seedNo, @RequestParam int maxJobCnt) {
        log.info("Detected unit crawl for #{} limit {}", seedNo, maxJobCnt);
        int scheduled = this.jobProducer.crawlContents(seedNo, maxJobCnt) ;

        return String.format("Job linked with seedNo:%s scheduled : %d", seedNo, scheduled);
    }

    @Deprecated
    @RequestMapping("/crawl/lsturls")
    public String crawlTargetUrlList(@RequestParam long seedNo) {
        log.info("Detected list crawl for seed #{}", seedNo);
        Job<ListUrlCrawllJob> lstCrawlJob = this.jobBuilder.createSeedListCrawlJob(seedNo);

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
                this.naverStockJobManager.execSerialNewsJobs(seedNo, start, end);
            }).start();
        } else {
            log.info("Invalid parameters : {} -> {}", start, end);
            return "Invalid params which should be yyyyMMdd format ..";
        }

        return "Naver Thread Started .." ;
    }

    @RequestMapping("/crawl/naver/kospi")
    public String crawlKospiIndexFromNaver() {
        log.info("Crawl All KOSPI Invester Values");
        long ts = System.currentTimeMillis() ;

        this.naverStockJobManager.execSerialJobsCrawlKospiInvest();

        return "Crawl Kospi Job Completed, estimated " + (System.currentTimeMillis() - ts);
    }

    /**
     * @param startPd PAGE_DATE > '2021-03-01' and PAGE_DATE < '2021-03-03';
     * @param endPd PAGE_DATE > '2021-03-01' and PAGE_DATE < '2021-03-03';
     * @return
     */
    @RequestMapping("/w2v/sim/build")
    public String simulBuildW2vFile(@RequestParam  String startPd, @RequestParam String endPd) {
        log.info("Request detected W2V model build : {} / {}", startPd, endPd);

        long ts = System.currentTimeMillis();
        this.word2vecModeler.simulBuildW2vModel(startPd, endPd);

        return "load completed for " + (System.currentTimeMillis() - ts) ;
    }

    /**
     * @param startPd PAGE_DATE > '2021-03-01' and PAGE_DATE < '2021-03-03';
     * @param endPd PAGE_DATE > '2021-03-01' and PAGE_DATE < '2021-03-03';
     * @return
     */
    @RequestMapping("/w2v/build")
    public String buildW2vFile(@RequestParam  String startPd,
                               @RequestParam String endPd,
                               @RequestParam(required = false) String filePath) {
        log.info("Request detected W2V model build : {} / {} -> {}", startPd, endPd, filePath);

        long ts = System.currentTimeMillis();
        this.word2vecModeler.buildW2vFile(startPd, endPd, filePath);

        return "load completed for " + (System.currentTimeMillis() - ts) ;
    }

    @RequestMapping("/p2v/simulate")
    public String paraVecSimulate() {
        try {
            this.topicCluster.makeParagraphVectors();
            this.topicCluster.checkUnlabeledData();
            return "completed ..";
        } catch (Exception e) {
            return "failed .." + e.getMessage();
        }
    }

}

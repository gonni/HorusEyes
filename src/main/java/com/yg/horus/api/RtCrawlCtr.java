package com.yg.horus.api;

import com.yg.horus.dto.Consumer;
import com.yg.horus.dto.CrawlStatus;
import com.yg.horus.scheduler.realtime.CrawlJobManager;
import com.yg.horus.scheduler.realtime.SelfJobProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class RtCrawlCtr {
    private final CrawlJobManager crawlJobManager ;

    @Autowired
    public RtCrawlCtr(CrawlJobManager _crawlJobManager) {
        this.crawlJobManager = _crawlJobManager ;
    }

    @RequestMapping("/crawl/admin/status/all")
    public @ResponseBody List<CrawlStatus> crawlStatus(@RequestParam(required = false) long seedNo) {
        log.info("Crawl status for seedNo:{}", seedNo);
        List<CrawlStatus> lstCrawlStaus = new ArrayList<>();

        Map<String, SelfJobProcessor> jps = this.crawlJobManager.getMapJobProcessors();
        if(jps != null) {
            jps.forEach((key, jp) -> {
                CrawlStatus cs = new CrawlStatus();

                cs.setCrawlStatus(CrawlStatus.CRAWL_STATUS.UNKNOWN);

                String[] tokens = key.split("_");
                cs.setDocType(CrawlJobManager.CRAWL_DOC_TYPE.valueOf(tokens[0]));
                cs.setSeedNo(Long.valueOf(tokens[1]));
                cs.setCntJobProcessed(jp.getCntJobProcTurns());
                cs.setCntJobFetched(jp.getCntProcessedUnit());
                // consume workers
                Consumer consumer = new Consumer(
                        jp.getCntWorkers(),
                        jp.getCntActiveWorkers(),
                        jp.getCntPendingJobs());

                cs.setConsumer(consumer);

                lstCrawlStaus.add(cs);
            });
        }

        return lstCrawlStaus;
    }

    // run periodically
    @RequestMapping("/crawl/rt/schedule")
    public @ResponseBody String rtBatchCrawl(@RequestParam long seedNo) {
        log.info("Start crawl seedNo:{}", seedNo);
        this.crawlJobManager.startJobProcessor(seedNo);
        return "MANAGED";
    }

    @RequestMapping("/crawl/unit/list")
    public @ResponseBody String unitCrawlList(@RequestParam long seedNo) {
        log.info("Start crawl seedNo:{}", seedNo);
        this.crawlJobManager.runListCrawlJob(seedNo);
        return "SUC";
    }

    @RequestMapping("/crawl/rt/list")
    public @ResponseBody String rtCrawlList(@RequestParam long seedNo) {
        log.info("Start crawl seedNo:{}", seedNo);
        this.crawlJobManager.runListCrawlJob(seedNo);
        return "SUC";
    }

    @RequestMapping("/crawl/unit/content")
    public @ResponseBody String unitCrawlContent(@RequestParam long seedNo) {
        log.info("Start crawl seedNo:{}", seedNo);
        this.crawlJobManager.runContentCrawlJob(seedNo, 10, 1000L);
        return "SUC";
    }

    @RequestMapping("/crawl/rt/content")
    public @ResponseBody String rtCrawlContent(@RequestParam long seedNo) {
        log.info("Start crawl seedNo:{}", seedNo);
        this.crawlJobManager.runContentCrawlJob(seedNo, 10, 1000L);
        return "SUC";
    }
}

package com.yg.horus.api;

import com.yg.horus.scheduler.realtime.CrawlJobManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RtCrawlCtr {
    private final CrawlJobManager crawlJobManager ;

    @Autowired
    public RtCrawlCtr(CrawlJobManager _crawlJobManager) {
        this.crawlJobManager = _crawlJobManager ;
    }

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

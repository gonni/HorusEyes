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

    @RequestMapping("/crawl/unit/list")
    public @ResponseBody String unitCrawlList(@RequestParam long seedNo) {
        log.info("Start crawl seedNo:{}", seedNo);
        this.crawlJobManager.runListCrawlJob(seedNo);
        return "SUC";
    }

    @RequestMapping("/crawl/unit/Content")
    public @ResponseBody String unitCrawlContent(@RequestParam long seedNo) {
        log.info("Start crawl seedNo:{}", seedNo);
        this.crawlJobManager.runContentCrawlJob(seedNo, 1000L);
        return "SUC";
    }
}

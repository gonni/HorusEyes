package com.yg.horus.scheduler.realtime;

import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.data.WrapperRule;
import com.yg.horus.dto.ListCrawlOption;
import com.yg.horus.scheduler.realtime.jobs.CrawlListJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrawlJobManager {
    private final CrawlRepository crawlRepository ;

    private JobProcessor listCrawlProcessor = null ;
    private JobProcessor contCrawlProcessor = null ;

    @Autowired
    public CrawlJobManager(CrawlRepository _crawlRepository) {
        this.crawlRepository = _crawlRepository ;
        this.listCrawlProcessor = new JobProcessor(2);
        this.listCrawlProcessor = new JobProcessor(3);
    }

    public void runListJob() {
        // crawl same seed page by period
        Optional<CrawlUnit> crawlUnit = this.crawlRepository.findById(1L);
        crawlUnit.map(a -> {
            List<WrapperRule> wrapperRules = a.getTopSeeds().getWrapperRules();
//            wrapperRules.get

            ListCrawlOption option = new ListCrawlOption();
            this.listCrawlProcessor.startJob(new CrawlListJob(option, this.crawlRepository));
            return 1;
        });
    }

    public void runContentJob() {
        // crawl stored seeds page list by short period compared to list
        ;
    }

    public void startJob(Job job) {
        ;
    }

}

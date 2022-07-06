package com.yg.horus.scheduler.realtime;

import com.yg.horus.crawl.ContentsPageCrawler;
import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.data.*;
import com.yg.horus.doc.ContentPageDoc;
import com.yg.horus.dto.ListCrawlOption;
import com.yg.horus.scheduler.global.DataConvHelper;
import com.yg.horus.scheduler.realtime.jobs.CrawlContentJob;
import com.yg.horus.scheduler.realtime.jobs.CrawlListJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CrawlJobManager {
    private static final long DEFAULT_CNT_CONTENT_CARWL = 100L;

    private final CrawlRepository crawlRepository ;
    private final WrapperRepository wrapperRepository ;
    private final DataConvHelper dataConvHelper ;

    private JobProcessor listCrawlProcessor = null ;
    private JobProcessor contCrawlProcessor = null ;

    @Autowired
    public CrawlJobManager(CrawlRepository _crawlRepository,
                           WrapperRepository _wrapperRepository,
                           DataConvHelper _dataConvHelper) {
        this.crawlRepository = _crawlRepository ;
        this.wrapperRepository = _wrapperRepository;
        this.dataConvHelper = _dataConvHelper;

        this.listCrawlProcessor = new JobProcessor(2);
        this.listCrawlProcessor.startWorkers();
        this.contCrawlProcessor = new JobProcessor(3);
        this.contCrawlProcessor.startWorkers();
    }

    public void runListCrawlJob(long seedNo) {
        // crawl same seed page by period
        Optional<CrawlUnit> crawlUnit = this.crawlRepository.findById(seedNo);
        crawlUnit.map(a -> {
            ListCrawlOption option = this.dataConvHelper.getListPageWrapRule(seedNo);
            this.listCrawlProcessor.startJob(new CrawlListJob(option, this.crawlRepository));
            return 1;
        });
    }

    public void runContentCrawlJob(long seedNo, long delay) {
        // crawl stored seeds page list by short period compared to list
        List<CrawlUnit> targetCrawlConts = this.crawlRepository.findByStatusAndTopSeedsSeedNoOrderByCrawlNoDesc(
                CrawlStatus.PEND, seedNo, Pageable.unpaged());
        log.info("Count of scheduled content crawls: {}", targetCrawlConts.size());

        ContentsPageWrappingRule contentPageWrapRule = this.dataConvHelper.getContentPageWrapRule(seedNo);

        targetCrawlConts.forEach(crawlUnit -> {
            CrawlContentJob contJob = new CrawlContentJob(crawlUnit, contentPageWrapRule, this.crawlRepository);
            this.contCrawlProcessor.startJob(contJob);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return ;
            }
        });

    }

}

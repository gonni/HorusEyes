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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CrawlJobManager {
    private static final long DEFAULT_CNT_CONTENT_CARWL = 100L;
    public enum CRAWL_DOC_TYPE {
        LIST,
        CONT,
        HYBR
    }

    private final SeedRepository seedRepository ;
    private final CrawlRepository crawlRepository ;
    private final WrapperRepository wrapperRepository ;
    private final DataConvHelper dataConvHelper ;

    private Map<String, SelfJobProcessor> mapJobProcessors ;

    @Autowired
    public CrawlJobManager(
            SeedRepository _seedRepository,
            CrawlRepository _crawlRepository,
            WrapperRepository _wrapperRepository,
            DataConvHelper _dataConvHelper) {
        this.seedRepository = _seedRepository ;
        this.crawlRepository = _crawlRepository ;
        this.wrapperRepository = _wrapperRepository;
        this.dataConvHelper = _dataConvHelper;
    }

    public void addJopProcessor(CRAWL_DOC_TYPE docType, long jobId, SelfJobProcessor jobProcessor) {
        if(this.mapJobProcessors == null)
            this.mapJobProcessors = new HashMap<>();
        this.mapJobProcessors.put(docType + "_" + jobId, jobProcessor) ;
    }

    public void startJobProcessor(long seedNo) {
        log.info("Start Job Scheduler #{}", seedNo);
        this.initNewListJob(seedNo, 2, 20000L);
        this.initNewContJob(seedNo, 3, 10000L);
    }

    private String getJobTypeCode(CRAWL_DOC_TYPE docType, long jobId) {
        return docType + "_" + jobId ;
    }

    public void stopAllJob(long seedNo) {
        this.stopJobProcessor(CRAWL_DOC_TYPE.LIST, seedNo);
        this.stopJobProcessor(CRAWL_DOC_TYPE.CONT, seedNo);

    }

    private void stopJobProcessor(CRAWL_DOC_TYPE type, long seedNo) {
        SelfJobProcessor lstProc =
                this.mapJobProcessors.get(this.getJobTypeCode(type, seedNo));
        if(lstProc != null) {
            lstProc.stopProducer();
            lstProc.terminateWorkers();
            log.info("JobProcessor successfully stopped ..");
            this.mapJobProcessors.remove(this.getJobTypeCode(type, seedNo)) ;
        }
    }

    public Map<String, SelfJobProcessor> getMapJobProcessors() {
        return this.mapJobProcessors ;
    }

    private void initNewListJob(long seedNo, int cntWorkers, long delay) {
        if(this.mapJobProcessors != null &&
                mapJobProcessors.get(CRAWL_DOC_TYPE.LIST + "_" + seedNo) != null) {
            log.info("The requested job already exists : {}", seedNo);
            return ;
        }

        SelfJobProcessor jobProcessor = new SelfJobProcessor(cntWorkers, delay) {
            @Override
            int createJob() {  // This method is called periodically
                // crawl same seed page by period
                runListCrawlJob(seedNo);
                return 1;
            }
        } ;

        jobProcessor.startWorkers();
        jobProcessor.startProducer();

        this.addJopProcessor(CRAWL_DOC_TYPE.LIST, seedNo, jobProcessor);
    }

    private void initNewContJob(long seedNo, int cntWorkers, long delay) {
        if(mapJobProcessors.get(CRAWL_DOC_TYPE.CONT + "_" + seedNo) != null) {
            log.info("The requested job already exists : {}", seedNo);
            return ;
        }

        SelfJobProcessor jobProcessor = new SelfJobProcessor(cntWorkers, delay) {
            @Override
            int createJob() {  // This method is called periodically
                if(super.queue.size() < 10) {
                    log.info("ContentCrawl worker queue size : {}", super.queue.size());
                    return runContentCrawlJob(seedNo, 20, 2000);
                } else
                    log.info("JobQueue is full ..");
                return 0;
            }
        } ;

        jobProcessor.startWorkers();
        jobProcessor.startProducer();

        this.addJopProcessor(CRAWL_DOC_TYPE.CONT, seedNo, jobProcessor);
    }


    public void runListCrawlJob(long seedNo) {
        // crawl same seed page by period
//        Optional<CrawlUnit> crawlUnit = this.crawlRepository.findById(seedNo);
//        log.info("CrawlUnit =>" + crawlUnit.toString());
//        JobProcessor listCrawlProcessor = this.mapJobProcessors.get(CRAWL_DOC_TYPE.LIST + "_" + seedNo) ;
//        crawlUnit.map(a -> {
//            ListCrawlOption option = this.dataConvHelper.getListPageWrapRule(seedNo);
//            listCrawlProcessor.startJob(new CrawlListJob(seedNo, option, this.crawlRepository));
//            return 1;
//        });
        Optional<TopSeeds> seedInfo = this.seedRepository.findById(seedNo);
        if(!seedInfo.isPresent()) {
            log.info("SeedInfo in HorusDB doesn't exist for #{}", seedNo);
            return ;
        }
        //TopSeeds seedInfo = this.seedRepository.findBySeedNo(seedNo);
        log.info("CrawlUnit =>" + seedInfo);
        JobProcessor listCrawlProcessor = this.mapJobProcessors.get(CRAWL_DOC_TYPE.LIST + "_" + seedNo) ;
        seedInfo.map(a -> {
            ListCrawlOption option = this.dataConvHelper.getListPageWrapRule(seedNo);
            listCrawlProcessor.startJob(new CrawlListJob(seedNo, option, this.crawlRepository));
            return 1;
        });


    }

    public int runContentCrawlJob(long seedNo, int limit, long delay) {
        // crawl stored seeds page list by short period compared to list
        List<CrawlUnit> targetCrawlConts = this.crawlRepository.findByStatusAndTopSeedsSeedNoOrderByCrawlNoAsc(
//                CrawlStatus.PEND, seedNo, Pageable.unpaged());
                CrawlStatus.IURL, seedNo, PageRequest.of(0, limit));
        log.info("Count of scheduled content crawls: {}", targetCrawlConts.size());

        ContentsPageWrappingRule contentPageWrapRule = this.dataConvHelper.getContentPageWrapRule(seedNo);
        JobProcessor contCrawlProcessor = this.mapJobProcessors.get(CRAWL_DOC_TYPE.CONT + "_" + seedNo) ;
        targetCrawlConts.forEach(crawlUnit -> {
            CrawlContentJob contJob = new CrawlContentJob(crawlUnit, contentPageWrapRule, this.crawlRepository);
            contCrawlProcessor.startJob(contJob);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return ;
            }
        });

        return (targetCrawlConts != null)? targetCrawlConts.size() : 0;
    }

}

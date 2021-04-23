package com.yg.horus.scheduler;

import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.data.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by a1000074 on 09/04/2021.
 */
@Slf4j
@Service
public class JobManager {

    private LinkedBlockingQueue<Job> singleWorkerQueue = null ;
    private LinkedBlockingQueue<Job> multiWorkerJobQueue = null ;
    @Autowired
    private CrawlRepository crawlRepository = null ;
    @Autowired
    private SeedRepository seedRepository = null ;
    @Autowired
    private WrapperRepository wrapperRepository = null ;

    public JobManager() {
        this.singleWorkerQueue = new LinkedBlockingQueue<>();
        this.multiWorkerJobQueue = new LinkedBlockingQueue<>();
    }

    public List<TopSeeds> getActiveTopSeeds() {
        return this.seedRepository.findByStatus(SeedStatus.ACTV) ;
    }

    public Job createSeedListCrawlJob(String url, long referSeedNo) {
//        TopSeeds seed = this.seedRepository.findBySeedNo(referSeedNo);
//        log.info("--> Detected Job Creation for Seed : {}", seed);
//        if(seed == null||
//                seed.getWrapperRules() == null ||
//                seed.getWrapperRules().size() < 1) {
//            return null ;
//        }

        ListUrlCrawllJob job = new ListUrlCrawllJob(url, referSeedNo);
        job.crawlRepository = this.crawlRepository ;

        List<WrapperRule> wrapperRules = this.wrapperRepository.findBySeedNo(referSeedNo) ;

        for(WrapperRule wr : wrapperRules) {
            WrapType wrapType = wr.getWrapType();

            if(wrapType.equals(WrapType.LIST_URL_TOP_AREA_FILTER)) {
                job.crawlUrlAreaQuery = wr.getWrapVal();
            } else if(wrapType.equals(WrapType.LIST_URL_PATTERN_FILTER)) {
                job.crawlUrlRegxPattern = wr.getWrapVal();
            }
        }

        return job ;
    }

    public Job createSeedListCrawlJob(long seedNo) {
        TopSeeds seed = this.seedRepository.findBySeedNo(seedNo);
        log.info("--> Detected Job Creation for Seed : {}", seed);
        if(seed == null||
                seed.getWrapperRules() == null ||
                seed.getWrapperRules().size() < 1) {
            return null ;
        }

        ListUrlCrawllJob job = new ListUrlCrawllJob(seed);
        job.crawlRepository = this.crawlRepository ;

        List<WrapperRule> wrapperRules = seed.getWrapperRules();
        for(WrapperRule wr : wrapperRules) {
            WrapType wrapType = wr.getWrapType();

            if(wrapType.equals(WrapType.LIST_URL_TOP_AREA_FILTER)) {
                job.crawlUrlAreaQuery = wr.getWrapVal();
            } else if(wrapType.equals(WrapType.LIST_URL_PATTERN_FILTER)) {
                job.crawlUrlRegxPattern = wr.getWrapVal();
            }
        }

        return job ;
    }

    public List<Job> createLatestContentsCrawlJobs(int topN) {
        List<Job> retJobs = new ArrayList<>() ;

        List<CrawlUnit> seed4cont = this.crawlRepository.findByStatusOrderByCrawlNoDesc(CrawlStatus.IURL, PageRequest.of(0, topN));

        for(CrawlUnit cu : seed4cont) {
            if(cu.getTopSeeds() != null && cu.getTopSeeds().getSeedNo() > 0) {
                List<WrapperRule> lstWrapperRule = this.wrapperRepository.findBySeedNo(cu.getTopSeeds().getSeedNo());
                ContentsPageWrappingRule wrapRule = new ContentsPageWrappingRule();

                for(WrapperRule wrule : lstWrapperRule) {
                    if(wrule.getWrapType().equals(WrapType.CONT_DATE_ON_PAGE)) {
                        wrapRule.setContDate(wrule.getWrapVal());
                    } else if(wrule.getWrapType().equals(WrapType.CONT_MAIN_CONT)) {
                        wrapRule.getContents().add(wrule.getWrapVal());
                    } else if(wrule.getWrapType().equals(WrapType.CONT_TITLE_ON_PAGE)) {
                        wrapRule.setTitleOnContents(wrule.getWrapVal());
                    }
                }

                // Create !!! ContentJob
//                retJobs.add(new ContentCrawlJob(cu.getUrl(), wrapRule, this.crawlRepository));
                retJobs.add(new ContentCrawlJob(cu, wrapRule, this.crawlRepository));
            }
        }

        return retJobs;
    }

    public static void main(String ... v) {
        System.out.println("Active Job Manager ..");

    }

}

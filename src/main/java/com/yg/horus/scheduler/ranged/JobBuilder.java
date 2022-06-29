package com.yg.horus.scheduler.ranged;

import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.data.*;
import com.yg.horus.scheduler.contcrawl.ContentCrawlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1000074 on 09/04/2021.
 */
@Slf4j
@Service
public class JobBuilder {

    @Autowired
    private CrawlRepository crawlRepository = null ;
    @Autowired
    private SeedRepository seedRepository = null ;
    @Autowired
    private WrapperRepository wrapperRepository = null ;

    public JobBuilder() {
//        this.singleWorkerQueue = new LinkedBlockingQueue<>();
//        this.multiWorkerJobQueue = new LinkedBlockingQueue<>();
    }

    public List<TopSeeds> getActiveTopSeeds() {
        return this.seedRepository.findByStatus(SeedStatus.ACTV) ;
    }


    public Job<CrawlUnit> createManagedSeedListCrawlJob() {
        return null ;
    }

    //TODO this can be used for serial job which crawls past data
    public Job<CrawlUnit> createSeedListCrawlJob(String url, long referSeedNo) {

        ListUrlCrawllJob job = new ListUrlCrawllJob(url, referSeedNo);
        job.crawlRepository = this.crawlRepository ;

        List<WrapperRule> wrapperRules = this.wrapperRepository.findBySeedNo(referSeedNo) ;

        return this.createJobWithRules(job, wrapperRules) ;
    }

    private Job createJobWithRules(ListUrlCrawllJob job, List<WrapperRule> wrapperRules) {
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



    public Job<ListUrlCrawllJob> createSeedListCrawlJob(long seedNo) {
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
        return this.createJobWithRules(job, wrapperRules) ;
    }

    public List<ContentCrawlJob> createLatestContentsCrawlJobs(long seedNo, int topN) {
        List<ContentCrawlJob> retJobs = new ArrayList<>() ;

        List<CrawlUnit> seed4cont = null ;
        if(seedNo > 0){
            seed4cont = this.crawlRepository.findByStatusAndTopSeedsSeedNoOrderByCrawlNoDesc(CrawlStatus.IURL, seedNo, PageRequest.of(0, topN));
        } else {
            seed4cont = this.crawlRepository.findByStatusOrderByCrawlNoDesc(CrawlStatus.IURL, PageRequest.of(0, topN));
        }

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

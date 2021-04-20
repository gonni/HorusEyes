package com.yg.horus.scheduler;

import com.yg.horus.data.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    public JobManager() {
        this.singleWorkerQueue = new LinkedBlockingQueue<>();
        this.multiWorkerJobQueue = new LinkedBlockingQueue<>();
    }

    private int addSingleWorkerJob(Job job) {

        return this.singleWorkerQueue.size() ;
    }

    private int addMultiWorkerJob(Job job) {

        return this.multiWorkerJobQueue.size() ;
    }

    public Job createSeedListCrawlJob(long seedNo) {
        TopSeeds seed = this.seedRepository.findBySeedNo(seedNo);
        log.info("--> Detected Job Creation for Seed : {}", seed);
        if(seed == null||
                seed.getWrapperRules() == null ||
                seed.getWrapperRules().size() < 1) {
            return null ;
        }

        ListUrlCrawllJob job = ListUrlCrawllJob.builder().seedUrl(seed.getUrlPattern()).build() ;
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

    public Job createContentCrawlJob() {
        return null ;
    }

    public Job createTimeRangeSeedCrawlJob() {
        return null ;
    }

    public static void main(String ... v) {
        System.out.println("Active Job Manager ..");

    }

}

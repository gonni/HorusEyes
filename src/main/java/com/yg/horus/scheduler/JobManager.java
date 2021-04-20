package com.yg.horus.scheduler;

import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.SeedRepository;
import com.yg.horus.data.TopSeeds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        log.info("--> Detected Seed : {}", seed);
        if(seed == null) return null ;


//        ListUrlCrawllJob job = new ListUrlCrawllJob();


        return null ;
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

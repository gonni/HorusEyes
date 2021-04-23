package com.yg.horus.scheduler;

import com.yg.horus.data.TopSeeds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by a1000074 on 23/04/2021.
 * TODO : Need to set Crontab Job
 */
@Service
@Slf4j
public class JobExecManager {
    @Autowired
    private JobManager jobManager = null ;
    @Autowired
    private JobScheduler jobScheduler = null;

    public JobExecManager() {
        ;
    }

    public void crawlSeedsList() {
        List<TopSeeds> activeTopSeeds = this.jobManager.getActiveTopSeeds();

        activeTopSeeds.forEach(seed -> {
            Job seedListCrawlJob = this.jobManager.createSeedListCrawlJob(seed.getSeedNo());

            this.jobScheduler.execute(seedListCrawlJob);
        });
        ;
    }

    /**
     * Get the seeds to crawl from DB.CRAWL_UNIT with IURL status
     */
    public void crawlContents() {
        ;
    }

}

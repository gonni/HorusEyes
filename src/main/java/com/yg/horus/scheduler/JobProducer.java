package com.yg.horus.scheduler;

import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
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
public class JobProducer {
    private final static int MAX_PENDING = 10000;
    @Autowired
    private JobManager jobManager = null ;
    @Autowired
    private JobScheduler jobScheduler = null;

    @Autowired
    private CrawlRepository crawlRepository = null;

    public JobProducer() {
        ;
    }


    //TODO need to be called by crobJob
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
        int cntJobs = 10;
        List<ContentCrawlJob> contJobs = this.jobManager.createLatestContentsCrawlJobs(-1L, cntJobs);
        log.info("Get New Job");
        contJobs.forEach(System.out::println);


    }

    public int crawlContents(long seedNo, int maxCrawlSize) {
        if(this.jobScheduler.getCntPendingJobs() > MAX_PENDING) {
            return -1;
        }


        int cntExcuted = 0;

        List<ContentCrawlJob> newContJobs = this.jobManager.createLatestContentsCrawlJobs(seedNo, maxCrawlSize);
        System.out.println("New Job : " + newContJobs.size());
        for(ContentCrawlJob job : newContJobs) {
            this.jobScheduler.execute(job);

            CrawlUnit crawlUnit = job.getCrawlUnit();
            crawlUnit.setStatus(CrawlStatus.PEND);

            this.crawlRepository.save(crawlUnit) ;
            cntExcuted++;
            log.info("CrawlContentJob scheduled :{}", crawlUnit.toString());
        }

        return cntExcuted;
    }

}

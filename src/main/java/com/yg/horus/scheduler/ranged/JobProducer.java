package com.yg.horus.scheduler.ranged;

import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.data.TopSeeds;
import com.yg.horus.scheduler.ranged.contcrawl.ContentCrawlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by a1000074 on 23/04/2021.
 * TODO : Need to set Crontab Job
 */
@Service
@Slf4j
public class JobProducer implements Runnable {

    private final static int DEFAULT_CRAWL_UNIT_COUNT = 10 ;
    private final static int MAX_PENDING = 10000;

    @Autowired
    private JobBuilder jobBuilder = null ;
    @Autowired
    private JobScheduler jobScheduler = null;

    @Autowired
    private CrawlRepository crawlRepository = null;

    private Thread worker = null ;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private LinkedHashSet<Long> seeds = new LinkedHashSet<>();

    public JobProducer() {
        ;
    }


    //TODO need to be called by crobJob
    public void crawlSeedsList() {
        List<TopSeeds> activeTopSeeds = this.jobBuilder.getActiveTopSeeds();

        activeTopSeeds.forEach(seed -> {
            Job seedListCrawlJob = this.jobBuilder.createSeedListCrawlJob(seed.getSeedNo());

            this.jobScheduler.execute(seedListCrawlJob);
        });
        ;
    }

    /**
     * Get the seeds to crawl from DB.CRAWL_UNIT with IURL status
     */
    public void crawlContents() {
        int cntJobs = 10;
        List<ContentCrawlJob> contJobs = this.jobBuilder.createLatestContentsCrawlJobs(-1L, cntJobs);
        log.info("Get New Job");
        contJobs.forEach(System.out::println);

    }

    //[CORE] Crawl Contents
    public int crawlContents(long seedNo, int maxCrawlSize) {
        if(this.jobScheduler.getCntPendingJobs() > MAX_PENDING) {
            return -1;
        }

        int cntExcuted = 0;

        List<ContentCrawlJob> newContJobs = this.jobBuilder.createLatestContentsCrawlJobs(seedNo, maxCrawlSize);
//        System.out.println("New Job : " + newContJobs.size());
        log.info("Count of Jobs to crawl contents : {}", newContJobs.size());
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

    public void addJobSet(long seedNo) {
        if(this.seeds != null)
            this.seeds = new LinkedHashSet<>();

        this.seeds.add(seedNo);
    }

    public synchronized void startWorker() {
        log.info("Start JobProducer Worker ..");
        this.worker = new Thread(this);
        this.running.set(true);
        this.worker.start();
    }

    public void stopWorker() {
        this.running.set(false);
    }

    @Override
    public void run() {
        while(this.running.get()) {
            if(this.jobScheduler.getCntPendingJobs() < 10) {
                this.seeds.forEach(seed -> {
                    int scheduled = this.crawlContents(seed, DEFAULT_CRAWL_UNIT_COUNT);
                    log.info("[JobProducer] target :{} -> scheduled :{}", seed, scheduled);
                });
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

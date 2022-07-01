package com.yg.horus.scheduler.realtime;

import com.yg.horus.data.CrawlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class CrawlJobManager {
    private final CrawlRepository crawlRepository ;
    // workers
    private LinkedBlockingQueue<Job> jobQueue = null ;

    @Autowired
    public CrawlJobManager(CrawlRepository _crawlRepository) {
        this.crawlRepository = _crawlRepository ;
        this.jobQueue = new LinkedBlockingQueue<Job>() ;
    }

    public void startJob(Job job) {

        ;
    }

}

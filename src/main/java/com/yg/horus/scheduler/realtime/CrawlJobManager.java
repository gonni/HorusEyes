package com.yg.horus.scheduler.realtime;

import java.util.concurrent.LinkedBlockingQueue;

public class CrawlJobManager {

    // workers
    private LinkedBlockingQueue<Job> jobQueue = null ;

    public CrawlJobManager() {
        this.jobQueue = new LinkedBlockingQueue<Job>() ;

    }

    public void startJob(Job job) {
        ;
    }
}

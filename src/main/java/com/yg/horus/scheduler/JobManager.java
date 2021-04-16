package com.yg.horus.scheduler;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by a1000074 on 09/04/2021.
 */
@Slf4j
public class JobManager {

    private LinkedBlockingQueue<Job> singleWorkerQueue = null ;
    private LinkedBlockingQueue<Job> multiWorkerJobQueue = null ;

    public JobManager() {
        this.singleWorkerQueue = new LinkedBlockingQueue<>();
        this.multiWorkerJobQueue = new LinkedBlockingQueue<>();
    }

    public int addSingleWorkerJob(Job job) {

        return this.singleWorkerQueue.size() ;
    }

    public int addMultiWorkerJob(Job job) {

        return this.multiWorkerJobQueue.size() ;
    }

    public Job createSeedCrawlJob() {
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

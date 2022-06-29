package com.yg.horus.scheduler.ranged;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by a1000074 on 22/04/2021.
 */
@Deprecated
@Service
@Slf4j
public class JobScheduler extends Thread implements Runnable, Observer {
    public static int COUNTER = 0;
    public final static int MAX_PENDING_JOB_COUNT = 10000;
    public final static long JOB_PRODUCE_PERIOD = 1000L;
    public final static int THREAD_POOL_SIZE = 10 ;

    private ThreadPoolExecutor executorService = null ;
    private LinkedBlockingQueue<Job> jobQueue = null ;
    private volatile boolean running = false ;

    public JobScheduler() {
        this.jobQueue = new LinkedBlockingQueue<>();
        this.executorService = new ThreadPoolExecutor(1, THREAD_POOL_SIZE ,3, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    public String getStatusMessage() {
        return String.format("Scheduled Jobs : %d, Pending Jobs : %d", COUNTER, this.getCntPendingJobs());
    }

    public synchronized int getCntPendingJobs() {
        return this.jobQueue.size();
    }

    /**
     * Job excution period between current and next at least JOB_PRODUCE_PERIOD milliSec.
     */
    @Override
    public void run() {
        while(this.running) {
            try {
                Job job = jobQueue.take();
                COUNTER++;
                log.info("New job scheduled : {}", job);

                this.executorService.execute(() -> {
                    job.start();
                });

                Thread.sleep(JOB_PRODUCE_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.info("Job Producing Error : {}", e.getMessage());
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ;
    }

    public synchronized boolean execute(Job job) {
        this.running = true;
        if(!this.isAlive()) {
            log.info("Start Main Thread ..");
            this.start();
        }

        try {
            if(this.jobQueue.size() + 1 < MAX_PENDING_JOB_COUNT) {
                this.jobQueue.put(job);
                return true;
            } else {
                log.info("Too many pending jobs in queue ..");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false ;
    }

    public void instantExec(Job job) {
        this.running = true;
        if(!this.isAlive()) {
            log.info("Start Main Thread ..");
            this.start();
        }

        this.executorService.execute(() -> {
            job.start();
        });
    }


    public static void main(String ... v) {
        System.out.println("Active System ..") ;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int idx = COUNTER ++;
                System.out.println(idx + " start at " + new Date(System.currentTimeMillis()));

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    ;
                }

                System.out.println(idx + " end at " + new Date(System.currentTimeMillis()));
            }
        } ;

        new Timer().schedule(task, 0, 1000L);
    }
}

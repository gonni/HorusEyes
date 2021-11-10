package com.yg.horus.scheduler;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class JobletProcessor implements Runnable {
    public static long MINIMUM_DELAY = 1000L ;
    private LinkedBlockingQueue<Joblet> jobletQueue = null ;

    private volatile boolean running = false ;
    private Thread worker = null ;

    private long count = 0L;
    private String name = null ;

    public JobletProcessor(String name) {
        this.jobletQueue = new LinkedBlockingQueue<>();
        this.name = name ;
    }

    public void schedule(Joblet joblet){
        log.info("added joblet : {}", joblet.toString());
        this.jobletQueue.offer(joblet);

    }

    private void process(Joblet joblet) {
        joblet.start();
    }

    @Override
    public void run() {
        while(this.running) {
            try {
                log.info("Waiting joblet .. {}, total: {}", this.count, this.jobletQueue.size());
                Joblet joblet = this.jobletQueue.take();
                log.info("{} handle Joblet : {}", this.name, this.count++);
                this.process(joblet);
                Thread.sleep(MINIMUM_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public void startWorker() {
        this.worker = new Thread(this);
        this.running = true ;
        this.worker.start();
    }
}

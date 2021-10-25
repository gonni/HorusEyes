package com.yg.horus.scheduler;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class JobletProcessor implements Runnable {
    private static long MINIMUM_DELAY = 1000L ;
    private LinkedBlockingQueue<Joblet> jobletQueue = null ;

    private volatile boolean running = false ;
    private Thread worker = new Thread(this);

    private long count = 0L;

    public JobletProcessor() {
        this.jobletQueue = new LinkedBlockingQueue<>();

    }

    public void schedule(Joblet joblet){
        synchronized(this.jobletQueue){
            this.jobletQueue.add(joblet);
        }
    }

    private void process(Joblet joblet) {
        joblet.start();
    }

    @Override
    public void run() {
        while(this.running) {
            try {
                Joblet joblet = this.jobletQueue.take();
                this.process(joblet);
                log.info("Handle Joblet : {}", this.count++);
                Thread.sleep(1000L);
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

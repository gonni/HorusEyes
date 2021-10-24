package com.yg.horus.scheduler;

import java.util.concurrent.LinkedBlockingQueue;

public class JobletProcessor implements Runnable {
    LinkedBlockingQueue<Joblet> jobletQueue = null ;

    public void schedule(Joblet joblet){
        synchronized(this.jobletQueue){
            this.jobletQueue.add(joblet);
        }
    }

    private void process() {

    }

    @Override
    public void run() {

    }
}

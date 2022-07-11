package com.yg.horus.scheduler.realtime;

import lombok.Getter;

abstract public class SelfJobProcessor extends JobProcessor implements Runnable {
    private volatile boolean running = false ;
    private Thread jobProducerWorker = null ;
    private long delay = 1000L ;
    @Getter
    private long cntJobProcTurns = 0L;
    @Getter
    private long cntProcessedUnit = 0L;

    SelfJobProcessor(int cntWorkers, long delay) {
        super(cntWorkers);
        this.delay = delay ;

    }

    public void startProducer() {
        if(this.jobProducerWorker == null || !this.jobProducerWorker.isAlive()) {
            this.jobProducerWorker = new Thread(this);
        }
        synchronized (this) {
            this.running = true ;
        }
        this.jobProducerWorker.start();
    }

    public void stopProducer() {
        if(this.jobProducerWorker != null && this.jobProducerWorker.isAlive()) {
            synchronized (this) {
                this.running = false ;
            }
            this.jobProducerWorker.interrupt();
        }
    }

    @Override
    public void run() {
        while(this.running) {
            int cntUnitJobs = this.createJob() ;

            this.cntJobProcTurns ++ ;
            this.cntProcessedUnit += cntUnitJobs ;

            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    abstract int createJob() ;
}

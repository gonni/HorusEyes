package com.yg.horus.scheduler.realtime;

abstract public class SelfJobProcessor extends JobProcessor implements Runnable {
    private volatile boolean running = false ;
    private Thread jobProducerWorker = null ;
    private long delay = 1000L ;

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
            this.createJob();
            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    abstract void createJob() ;
}

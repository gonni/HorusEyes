package com.yg.horus.scheduler.realtime;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class JobProcessor {
    protected LinkedBlockingQueue<Job> queue = null ;
    private List<Worker> workers = null ;
    private int cntWorkers = 2;
    private static int jobIdCnt = 0;

    JobProcessor(int cntWorkers) {
        this.queue = new LinkedBlockingQueue<>();
        if(cntWorkers > 0) {
            this.cntWorkers = cntWorkers;
        }
        this.initWorkers();
    }

    private void initWorkers() {
        this.workers = new ArrayList<>(this.cntWorkers);

        for(int i=0;i<this.cntWorkers;i++) {
            this.workers.add(new Worker(jobIdCnt++));
        }
    }

    class Worker extends Thread {
        private int workerId = -1;
        private volatile boolean isRunning = false ;

        Worker(int id) {
            workerId = id ;
        }

        public void run() {
            Job iJob = null ;
            while(this.isRunning) {
                try {
                    iJob = queue.take();
                    if(iJob != null) {
                        log.info("Start job by worker #{}", workerId);
                        iJob.start();
                    }
                } catch (Exception e) {
                    log.info("Exception occurred ..", workerId);
                }
            }

        }

        public void stopWorker() {
            synchronized (this) {
                isRunning = false ;
            }
        }

        public void startWorker() {
            synchronized (this) {
                isRunning = true ;
            }
            super.start();
        }
    }

    public void startWorkers() {
        this.workers.forEach(Worker::startWorker);
    }

    public void startJob(Job job) {
        synchronized (this.queue) {
            this.queue.add(job);
            log.info("Count of pending jobs : {}", this.queue.size());
        }
    }

    public void terminateWorkers() {
        this.queue.clear();
        this.workers.forEach(Worker::stopWorker);
        this.workers.forEach(Worker::interrupt);

        log.info("All workers are just terminated ..");
    }

    public static void main(String ... v) throws Exception {
        System.out.println("Active System .. ");

        class SampleJob implements Job {
            private int jobIdi = 0;
            public SampleJob(int id) {
                jobIdi = id ;
            }

            @Override
            public void start() {
                try { Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(jobIdi + "Job Started .. " + System.currentTimeMillis()) ;
            }

            @Override
            public Object getCrawled() {
                return null;
            }
        }

        JobProcessor test = new JobProcessor(3);
        test.startWorkers();

        Thread.sleep(1000L);
        for(int i=0;i<10;i++)
            test.startJob(new SampleJob(i));

        Thread.sleep(13000L);
        test.terminateWorkers();
    }

}

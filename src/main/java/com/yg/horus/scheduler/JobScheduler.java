package com.yg.horus.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by a1000074 on 22/04/2021.
 */
@Service
@Slf4j
public class JobScheduler implements Runnable, Observer {
    public static int COUNTER = 0;
    public final static int THREAD_POOL_SIZE = 10 ;

//    private ScheduledExecutorService scheduler = null ;
    private LinkedBlockingQueue<Job> jobQueue = null ;


    public JobScheduler() {
//        this.scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
//        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        this.jobQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void schedule(List<Job> job) {

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

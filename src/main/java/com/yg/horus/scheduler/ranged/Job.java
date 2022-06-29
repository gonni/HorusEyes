package com.yg.horus.scheduler.ranged;

/**
 * Created by a1000074 on 18/03/2021.
 */
public interface Job<R> {
//    void start() ;
    R start();
    JobStatus getStatus();
    long getSeedId();
}

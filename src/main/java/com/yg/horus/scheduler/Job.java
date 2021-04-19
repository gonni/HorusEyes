package com.yg.horus.scheduler;

/**
 * Created by a1000074 on 18/03/2021.
 */
public interface Job {
    void start() ;
    JobStatus getStatus();
}

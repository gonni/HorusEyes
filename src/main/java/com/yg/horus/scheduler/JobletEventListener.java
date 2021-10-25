package com.yg.horus.scheduler;

/**
 * Created by a1000074 on 25/10/2021.
 */
public interface JobletEventListener<R> {
    void eventOccurred(Joblet joblet, Joblet.JOBLET_STATUS status, R result);
}

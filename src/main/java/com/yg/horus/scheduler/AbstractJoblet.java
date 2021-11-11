package com.yg.horus.scheduler;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * Created by a1000074 on 25/10/2021.
 */
@Slf4j
public abstract class AbstractJoblet<R> implements Joblet<R> {

    protected ArrayList<JobletEventListener> subscribedJoblets = null ;

    protected AbstractJoblet() {
        this.subscribedJoblets = new ArrayList<>();
    }

    public void addActionListener(JobletEventListener listener) {
        log.info("Add Joblet EV listener : {}", listener.toString());
        this.subscribedJoblets.add(listener) ;
    }

    public void updateAll(JOBLET_STATUS jobletStatus, R result) {
        this.subscribedJoblets.forEach(ev -> {
            ev.eventOccurred(this, jobletStatus, result);
        });
    }

}

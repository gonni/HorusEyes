package com.yg.horus.scheduler.ranged;

public interface Joblet<R> {
    enum JOBLET_STATUS {
        COMPLETED,
        NULL,
        FAILED
    }

    R start();
    JOBLET_STATUS getStatus();

    void addActionListener(JobletEventListener listener);
}

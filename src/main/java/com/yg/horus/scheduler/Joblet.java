package com.yg.horus.scheduler;

public interface Joblet<R> {
    interface JobletStatus<R> {
        enum JOBLET_STATUS {
            SUCCESS,
            NULL,
            FAILED
        }
        JOBLET_STATUS getStatus() ;
        R getResult() ;
    }

    R start();
    void onCompleted(R r);
}

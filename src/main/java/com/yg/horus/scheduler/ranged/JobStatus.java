package com.yg.horus.scheduler.ranged;

/**
 * Created by a1000074 on 19/04/2021.
 */
public enum JobStatus {
    INIT,
    PENDING,
    PROCESSING,
    COMPLETED,
    COMPLETED_NORESULT,
    FAILED,
    NOT_AVAILABLE
}

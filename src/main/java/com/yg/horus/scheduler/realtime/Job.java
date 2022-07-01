package com.yg.horus.scheduler.realtime;

public interface Job<R> {
    void start() ;
    R getCrawled() ;
}

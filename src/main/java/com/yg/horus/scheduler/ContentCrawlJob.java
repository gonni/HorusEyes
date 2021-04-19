package com.yg.horus.scheduler;

import java.util.Observable;

/**
 * Created by jeff on 21. 4. 18.
 */
public class ContentCrawlJob extends Observable implements Job {
    @Override
    public void start() {

    }

    @Override
    public JobStatus getStatus() {
        return null ;
    }
}

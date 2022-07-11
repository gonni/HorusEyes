package com.yg.horus.dto;

import com.yg.horus.scheduler.realtime.CrawlJobManager;
import lombok.Builder;
import lombok.Data;

@Data
public class CrawlStatus {
    public enum CRAWL_STATUS {
        RUNNING,
        PAUSED,
        STOPPED,
        BROKEN,
        UNKNOWN
    }

    private CrawlJobManager.CRAWL_DOC_TYPE docType ;
    private CRAWL_STATUS crawlStatus ;
    private long seedNo ;
    private Consumer consumer ;

    private long cntJobProcessed ;
    private long cntJobFetched ;

}

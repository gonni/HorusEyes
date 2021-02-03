package com.yg.horus.crawl;

import lombok.Data;

/**
 * Created by a1000074 on 25/01/2021.
 */
@Data
public class CrawlDataUnit {
    private String anchorText ;
    private String url ;

    public CrawlDataUnit(String anchorText, String url) {
        this.anchorText = anchorText ;
        this.url = url ;
    }

    public String toString() {
        return this.anchorText + "\t-->\t" + this.url ;
    }
}

package com.yg.horus.crawl;

import lombok.Data;

/**
 * Created by a1000074 on 25/01/2021.
 */
@Data
public class CrawlDataUnit {
    public enum AnchorType {
        TEXT,
        IMG,
        ETC
    }

    private AnchorType anchorType ;
    private String anchorText ;
    private String url ;

    public CrawlDataUnit(AnchorType anchorType, String anchorText, String url) {
        this.anchorType = anchorType ;
        this.anchorText = anchorText ;
        this.url = url ;
    }

    public String toString() {
        return this.anchorType + " : " + this.anchorText + "\t-->\t" + this.url ;
    }
}

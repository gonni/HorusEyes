package com.yg.horus.dto;

import lombok.Data;

@Data
public class ListCrawlOption {
    private String targetSeedUrl ;
    private String filterCrawlUrlRxPattern ;
    private String filterDomGroupAttr ;

}

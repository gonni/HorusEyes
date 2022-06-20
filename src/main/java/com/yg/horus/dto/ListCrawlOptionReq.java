package com.yg.horus.dto;

import lombok.Data;

@Data
public class ListCrawlOptionReq {
    private String targetSeedUrl ;
    private String filterCrawlUrlRxPattern ;
    private String filterDomGroupAttr ;

}

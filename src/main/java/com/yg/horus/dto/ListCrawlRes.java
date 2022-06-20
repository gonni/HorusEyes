package com.yg.horus.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListCrawlRes {
    private List<ListCrawl> lstCrawls ;
}

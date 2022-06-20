package com.yg.horus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListCrawl {
    private String url ;
    private String anchorText ;

}